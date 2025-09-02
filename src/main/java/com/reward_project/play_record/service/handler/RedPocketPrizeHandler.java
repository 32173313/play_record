package com.reward_project.play_record.service.handler;

import com.reward_project.play_record.controller.vo.BaseVO;
import com.reward_project.play_record.entity.PrizeRecord;
import com.reward_project.play_record.integration.SendRedPocketIntegration;
import com.reward_project.play_record.integration.cmd.SendRedPocketCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("red_pocket")
public class RedPocketPrizeHandler extends AbstractPrizeHandler {
    @Autowired
    private SendRedPocketIntegration sendRedPocketIntegration;


    @Override
    public BaseVO send(PrizeRecord prizeRecord) {
        SendRedPocketCmd sendRedPocketCmd = buildSendRedPocketCmd(prizeRecord);
        return sendRedPocketIntegration.sendRedPocketIntegration(sendRedPocketCmd);
    }


    private static SendRedPocketCmd buildSendRedPocketCmd(PrizeRecord prizeRecord) {
        SendRedPocketCmd sendRedPocketCmd = new SendRedPocketCmd();
        sendRedPocketCmd.setUserId(prizeRecord.getUserId());
        sendRedPocketCmd.setTheme(prizeRecord.getTheme());
        sendRedPocketCmd.setPrizeAmount(prizeRecord.getPrizeAmount());
        sendRedPocketCmd.setPrizeType(prizeRecord.getPrizeType());
        sendRedPocketCmd.setPrizeSubType(prizeRecord.getPrizeSubType());
        sendRedPocketCmd.setOutBizNo(prizeRecord.getOutBizNo());
        return sendRedPocketCmd;
    }
}
