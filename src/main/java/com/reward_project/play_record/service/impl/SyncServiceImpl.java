package com.reward_project.play_record.service.impl;

import com.reward_project.play_record.constant.RewardRuleNameConstant;
import com.reward_project.play_record.constant.RuleConstant;
import com.reward_project.play_record.controller.cmd.PlayRecordCmd;
import com.reward_project.play_record.controller.vo.BaseVO;
import com.reward_project.play_record.entity.PlayRecordSummary;
import com.reward_project.play_record.entity.PrizeRecord;
import com.reward_project.play_record.entity.PrizeStageInfo;
import com.reward_project.play_record.entity.RewardRule;
import com.reward_project.play_record.enums.DateFormatPatternEnum;
import com.reward_project.play_record.enums.PrizeTypeEnum;
import com.reward_project.play_record.exception.PrizeTypeNotExistException;
import com.reward_project.play_record.exception.SendCoinFailureException;
import com.reward_project.play_record.integration.SendCoinIntegration;
import com.reward_project.play_record.integration.SendCreditIntegration;
import com.reward_project.play_record.integration.SendRedPocketIntegration;
import com.reward_project.play_record.service.*;
import com.reward_project.play_record.service.handler.*;
import com.reward_project.play_record.service.producer.RewardRetryProducer;
import com.reward_project.play_record.util.RuleMatchUtil;
import com.reward_project.play_record.util.TimeConverterUtil;
import org.apache.commons.jexl3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class SyncServiceImpl implements SyncService {
    @Autowired
    private PlayRecordService playRecordService;


    @Autowired
    private PlayRecordSummaryService playRecordSummaryService;

    @Autowired
    private PrizeRecordService prizeRecordService;

    @Autowired
    private SendCoinIntegration sendCoinIntegration;

    @Autowired
    private RewardRetryProducer rewardRetryProducer;

    @Autowired
    private SendCreditIntegration sendCreditIntegration;

    @Autowired
    private SendRedPocketIntegration sendRedPocketIntegration;


    @Autowired
    private CoinPrizeHandler coinPrizeHandler;

    @Autowired
    private RedPocketPrizeHandler redPocketPrizeHandler;

    @Autowired
    private CreditPrizeHandler creditPrizeHandler;

    @Autowired
    private PrizeHandlerFactory prizeHandlerFactory;

    @Value("${prize-stage-rule}")
    private String prizeStageRule;

    @Value("${prize-amount-rule}")
    private String prizeAmountRule;

    @Autowired
    private RewardRuleService rewardRuleService;


    Logger logger = LoggerFactory.getLogger(SyncServiceImpl.class);

    @Override
    // 整个方法不用transactional 只要第一步插入 之后每次都会汇总总时长
    // 时间上报必须成功 否则少计时长
    public void syncProcess(PlayRecordCmd playRecordCmd) {
        // 第一步：上报时间
        playRecordService.addPlayRecord(playRecordCmd);

        // 第二步：计算总时长并在数据库更新（没有就插入 有就更新总时长）
        long totalDuration = playRecordService.totalDurationByDay(playRecordCmd.getUserId(), playRecordCmd.getPrizeType(), playRecordCmd.getPrizeSubType());

        // 保证每天每个用户只有一条记录
        // 需要保存总时长
        // redis 是内存数据库，怕丢，所以存去数据库而不是redis
        // 保证唯一性的方法：
        // 一锁：加锁版本的查询 - 写在数据库mapper层
        // 二判：判断是否为空
        // 三更新：不为空进行更新

        PlayRecordSummary playRecordSummary = playRecordSummaryService.queryPlayRecordSummaryForUpdate(playRecordCmd.getUserId(), TimeConverterUtil.formatDay(new Date(), DateFormatPatternEnum.DATE_FORMAT_PATTERN_ENUM.getCode()), playRecordCmd.getPrizeType(), playRecordCmd.getPrizeSubType());

        PlayRecordSummary playRecordSummary1 = buildPlayRecordSummary(playRecordCmd.getUserId(), totalDuration, playRecordCmd.getPrizeType(), playRecordCmd.getPrizeSubType());

        if (playRecordSummary == null) {
            logger.info(String.format("用户%d在%s第一次插入总时长", playRecordCmd.getUserId(), playRecordSummary1.getCurrentDate()));
            playRecordSummaryService.addPlayRecordSummary(playRecordSummary1);
        } else {
            playRecordSummaryService.updatePlayRecordSummary(playRecordSummary1);
        }
        // 构建发奖记录
        PrizeRecord prizeRecord = buildPrizeRecord(playRecordCmd, totalDuration);
        if (prizeRecord == null) {
            return;
        }
        // 第三四步：调下游发奖 并发奖记录插入数据库 为transactional (发奖和插入发奖记录同时成功同时失败）
        // 并加重试发奖（在网络有问题的情况下）
        try {
            sendCoinAndAddPrizeRecord(prizeRecord);
        } catch (DuplicateKeyException e) {
            logger.error(String.format("用户%d当前播放总时长为%d，本阶段已发奖，外部业务号为%s", prizeRecord.getUserId(), totalDuration, prizeRecord.getOutBizNo()));
        } catch (RuntimeException e) {
            rewardRetryProducer.sender(prizeRecord);
        }

        // 第三四步后 transactional（发奖和插入发奖记录同时成功同时失败） 并加重试发奖（在网络有问题的情况下）
    }

    // 重试和事务类似：都是基于异常决定是否操作
    // 有异常 则重试 所以要重试的方法里不能抛出异常
    // 有异常 则事务回滚 所以异常也不能catch
    @Transactional
    public void sendCoinAndAddPrizeRecord(PrizeRecord prizeRecord) {
        AbstractPrizeHandler handler = prizeHandlerFactory.getHandlerByType(prizeRecord.getPrizeType());

        if (handler == null) {
            logger.error(String.format("输入的奖品类型%s不存在", prizeRecord.getPrizeType()));
            throw new PrizeTypeNotExistException(String.format("输入的奖品类型%s不存在", prizeRecord.getPrizeType()));
        }

        handler.sendPrize(prizeRecord);


        // 第三步：调下游发奖
//        if (prizeRecord.getPrizeType().equals(PrizeTypeEnum.COIN.getCode())) {
//            coinPrizeHandler.sendPrize(prizeRecord);
//        } else if (prizeRecord.getPrizeType().equals(PrizeTypeEnum.CREDIT.getCode())) {
//            creditPrizeHandler.sendPrize(prizeRecord);
//        } else if (prizeRecord.getPrizeType().equals(PrizeTypeEnum.RED_POCKET.getCode())) {
//            redPocketPrizeHandler.sendPrize(prizeRecord);
//        } else {
//            logger.error(String.format("输入的奖品类型%s不存在", prizeRecord.getPrizeType()));
//            throw new PrizeTypeNotExistException(String.format("输入的奖品类型%s不存在", prizeRecord.getPrizeType()));
//        }


        // SendCoinCmd sendCoinCmd = buildSendCoinCmd(prizeRecord);
        // BaseVO baseVO = sendCoinIntegration.sendCoinIntegration(sendCoinCmd);

        // SendCreditCmd sendCreditCmd = buildSendCreditCmd(prizeRecord);
        // BaseVO baseVO = sendCreditIntegration.sendCreditIntegration(sendCreditCmd);

        // SendRedPocketCmd sendRedPocketCmd = buildSendRedPocketCmd(prizeRecord);
        // BaseVO baseVO = sendRedPocketIntegration.sendRedPocketIntegration(sendRedPocketCmd);

//        if (!baseVO.isSuccess()) {
//            logger.error(String.format("调用下游发奖失败，外部业务号为%s，失败原因为%s", prizeRecord.getOutBizNo(), baseVO.getErrorMsg()));
//            throw new SendCoinFailureException(String.format("调用下游发奖失败，外部业务号为%s，失败原因为%s", prizeRecord.getOutBizNo(), baseVO.getErrorMsg()));
//        }
        // 第四步：发奖记录插入数据库
//        prizeRecordService.addPrizeRecord(prizeRecord);
    }
//
//    private static SendRedPocketCmd buildSendRedPocketCmd(PrizeRecord prizeRecord) {
//        SendRedPocketCmd sendRedPocketCmd = new SendRedPocketCmd();
//        sendRedPocketCmd.setUserId(prizeRecord.getUserId());
//        sendRedPocketCmd.setTheme(prizeRecord.getTheme());
//        sendRedPocketCmd.setPrizeAmount(prizeRecord.getPrizeAmount());
//        sendRedPocketCmd.setPrizeType(prizeRecord.getPrizeType());
//        sendRedPocketCmd.setPrizeSubType(prizeRecord.getPrizeSubType());
//        sendRedPocketCmd.setOutBizNo(prizeRecord.getOutBizNo());
//        return sendRedPocketCmd;
//    }
//
//    private static SendCreditCmd buildSendCreditCmd(PrizeRecord prizeRecord) {
//        SendCreditCmd sendCreditCmd = new SendCreditCmd();
//        sendCreditCmd.setUserId(prizeRecord.getUserId());
//        sendCreditCmd.setTheme(prizeRecord.getTheme());
//        sendCreditCmd.setPrizeAmount(prizeRecord.getPrizeAmount());
//        sendCreditCmd.setPrizeType(prizeRecord.getPrizeType());
//        sendCreditCmd.setPrizeSubType(prizeRecord.getPrizeSubType());
//        sendCreditCmd.setOutBizNo(prizeRecord.getOutBizNo());
//        return sendCreditCmd;
//    }
//
//    private static SendCoinCmd buildSendCoinCmd(PrizeRecord prizeRecord) {
//        SendCoinCmd sendCoinCmd = new SendCoinCmd();
//        sendCoinCmd.setUserId(prizeRecord.getUserId());
//        sendCoinCmd.setTheme(prizeRecord.getTheme());
//        sendCoinCmd.setPrizeAmount(prizeRecord.getPrizeAmount());
//        sendCoinCmd.setPrizeType(prizeRecord.getPrizeType());
//        sendCoinCmd.setPrizeSubType(prizeRecord.getPrizeSubType());
//        sendCoinCmd.setOutBizNo(prizeRecord.getOutBizNo());
//        return sendCoinCmd;
//    }

    private PrizeRecord buildPrizeRecord(PlayRecordCmd playRecordCmd, long totalDuration) {
        PrizeRecord prizeRecord = new PrizeRecord();
        PrizeStageInfo prizeStageInfo = buildPrizeStageInfo(totalDuration, playRecordCmd);
        if (prizeStageInfo.getStage() == 0) {
            logger.warn(String.format("用户%d当前播放总时长为%d，未达到最低发奖门槛", playRecordCmd.getUserId(), totalDuration));
            return null;
        }
        String date = TimeConverterUtil.formatDay(new Date(), DateFormatPatternEnum.DATE_FORMAT_PATTERN_ENUM.getCode());

        prizeRecord.setUserId(playRecordCmd.getUserId());
        prizeRecord.setDate(date);
        prizeRecord.setPrizeAmount(prizeStageInfo.getPrizeAmount());
        prizeRecord.setStage(prizeStageInfo.getStage());
        prizeRecord.setTheme(playRecordCmd.getTheme());
        prizeRecord.setPrizeType(playRecordCmd.getPrizeType());
        prizeRecord.setPrizeSubType(playRecordCmd.getPrizeSubType());

        String outBizNo = buildOutBizNo(playRecordCmd.getTheme(), playRecordCmd.getUserId(), date, playRecordCmd.getPrizeType(), playRecordCmd.getPrizeSubType(), prizeStageInfo.getStage());
        prizeRecord.setOutBizNo(outBizNo);
        return prizeRecord;
    }

    private static PlayRecordSummary buildPlayRecordSummary(int userId, long totalDuration, String type, String subType) {
        PlayRecordSummary playRecordSummary1 = new PlayRecordSummary();
        playRecordSummary1.setUserId(userId);
        playRecordSummary1.setTotalDuration(totalDuration);
        playRecordSummary1.setCurrentDate(TimeConverterUtil.formatDay(new Date(), DateFormatPatternEnum.DATE_FORMAT_PATTERN_ENUM.getCode()));
        playRecordSummary1.setType(type);
        playRecordSummary1.setSubType(subType);
        return playRecordSummary1;
    }

    private PrizeStageInfo buildPrizeStageInfo(long totalDuration, PlayRecordCmd playRecordCmd) {
        HashMap<String, Long> ruleMatchMap = new HashMap<>();
        ruleMatchMap.put("totalDuration", totalDuration);
        ruleMatchMap.put("prizeAmount", 0L);
        ruleMatchMap.put("stage", 0L);

        RewardRule stageRule = rewardRuleService.queryByName(RewardRuleNameConstant.STAGE_RULE);
        RewardRule prizeAmountRule = rewardRuleService.queryByName(RewardRuleNameConstant.PRIZE_AMOUNT_RULE);

        int stage = (int) RuleMatchUtil.buildPrizeStageInfol(stageRule.getRule(), ruleMatchMap);
        int prizeAmount = (int) RuleMatchUtil.buildPrizeStageInfol(prizeAmountRule.getRule(), ruleMatchMap);

        return new PrizeStageInfo(stage, prizeAmount);
    }


    // 在哪个场景 给哪个用户 在哪一天 发了哪种奖励 在哪个阶段
    private String buildOutBizNo (String theme, int userId, String date, String prizeType, String prizeSubType, int stage) {
        return theme + "_" + userId + "_" + date + "_" + prizeType + "_" + prizeSubType + "_" + stage;
    }

//    private PrizeStageInfo buildPrizeStageInfo1(long totalDuration, PlayRecordCmd playRecordCmd) {
//        try {
//            JexlEngine jexl = new JexlBuilder().create();
//            String jexlExpStage = RuleConstant.STAGE_RULE;
//            JexlExpression expStage = jexl.createExpression(jexlExpStage);
//
//            String jexlExpPA = RuleConstant.PRIZE_AMOUNT_RULE;
//            JexlExpression expPA = jexl.createExpression(jexlExpPA);
//
//            JexlContext jc = new MapContext();
//            jc.set("totalDuration", totalDuration);
//            jc.set("prizeAmount", 0);
//            jc.set("stage", 0);
//
//            int stage = (Integer) expStage.evaluate(jc);
//            int prizeAmount = (Integer) expPA.evaluate(jc);
//
//            return new PrizeStageInfo(stage, prizeAmount);
//
//        } catch (Exception e) {
//            logger.error(String.format("给用户%d在%s主题下计算发奖阶段信息失败", playRecordCmd.getUserId(), playRecordCmd.getTheme()));
//            return null;
//        }
//    }


//    private PrizeStageInfo buildPrizeStageInfo (long totalDuration) {
//        PrizeStageInfo prizeStageInfo = new PrizeStageInfo();
//        if (totalDuration < 160L) {
//            prizeStageInfo.setStage(0);
//            prizeStageInfo.setPrizeAmount(0);
//        } else if (totalDuration < 320L) {
//            prizeStageInfo.setStage(1);
//            prizeStageInfo.setPrizeAmount(1);
//        } else if (totalDuration < 640L) {
//            prizeStageInfo.setStage(2);
//            prizeStageInfo.setPrizeAmount(1);
//        } else if (totalDuration < 960L) {
//            prizeStageInfo.setStage(3);
//            prizeStageInfo.setPrizeAmount(2);
//        } else if (totalDuration < 1280L) {
//            prizeStageInfo.setStage(4);
//            prizeStageInfo.setPrizeAmount(2);
//        } else if (totalDuration < 1600L) {
//            prizeStageInfo.setStage(5);
//            prizeStageInfo.setPrizeAmount(2);
//        } else if (totalDuration < 3200L) {
//            prizeStageInfo.setStage(6);
//            prizeStageInfo.setPrizeAmount(2);
//        } else if (totalDuration < 6400L) {
//            prizeStageInfo.setStage(7);
//            prizeStageInfo.setPrizeAmount(10);
//        } else {
//            prizeStageInfo.setStage(8);
//            prizeStageInfo.setPrizeAmount(10);
//        }
//        return prizeStageInfo;
//    }


}
