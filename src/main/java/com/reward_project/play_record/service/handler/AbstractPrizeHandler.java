package com.reward_project.play_record.service.handler;

import com.reward_project.play_record.controller.vo.BaseVO;
import com.reward_project.play_record.entity.PrizeRecord;
import com.reward_project.play_record.exception.SendCoinFailureException;
import com.reward_project.play_record.service.PrizeRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class AbstractPrizeHandler {

    @Autowired
    private PrizeRecordService prizeRecordService;

    Logger logger = LoggerFactory.getLogger(AbstractPrizeHandler.class);

    // 抽象类里的非抽象方法
    public void sendPrize(PrizeRecord prizeRecord) {
        logger.info(String.format("在场景%s下，给用户%d发第%d阶段，类型为%s子类型为%s的奖品，数量为%d，外部业务号为%s", prizeRecord.getTheme(), prizeRecord.getUserId(), prizeRecord.getStage(), prizeRecord.getPrizeType(), prizeRecord.getPrizeSubType(), prizeRecord.getPrizeAmount(), prizeRecord.getOutBizNo()));

        BaseVO baseVO = send(prizeRecord);

        if (!baseVO.isSuccess()) {
            logger.error(String.format("调用下游发奖失败，外部业务号为%s，失败原因为%s", prizeRecord.getOutBizNo(), baseVO.getErrorMsg()));
            throw new SendCoinFailureException(String.format("调用下游发奖失败，外部业务号为%s，失败原因为%s", prizeRecord.getOutBizNo(), baseVO.getErrorMsg()));
        }
        prizeRecordService.addPrizeRecord(prizeRecord);
    }

    public abstract BaseVO send(PrizeRecord prizeRecord);
}
