package com.reward_project.play_record.service.handler;

import com.reward_project.play_record.controller.vo.BaseVO;
import com.reward_project.play_record.entity.PrizeRecord;
import com.reward_project.play_record.integration.SendCreditIntegration;
import com.reward_project.play_record.integration.cmd.SendCreditCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("credit")
public class CreditPrizeHandler extends AbstractPrizeHandler{

    @Autowired
    private SendCreditIntegration sendCreditIntegration;


    @Override
    public BaseVO send(PrizeRecord prizeRecord) {
        SendCreditCmd sendCreditCmd = buildSendCreditCmd(prizeRecord);
        return sendCreditIntegration.sendCreditIntegration(sendCreditCmd);
    }


    private static SendCreditCmd buildSendCreditCmd(PrizeRecord prizeRecord) {
        SendCreditCmd sendCreditCmd = new SendCreditCmd();
        sendCreditCmd.setUserId(prizeRecord.getUserId());
        sendCreditCmd.setTheme(prizeRecord.getTheme());
        sendCreditCmd.setPrizeAmount(prizeRecord.getPrizeAmount());
        sendCreditCmd.setPrizeType(prizeRecord.getPrizeType());
        sendCreditCmd.setPrizeSubType(prizeRecord.getPrizeSubType());
        sendCreditCmd.setOutBizNo(prizeRecord.getOutBizNo());
        return sendCreditCmd;
    }
}
