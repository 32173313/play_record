package com.reward_project.play_record.service.handler;


import com.reward_project.play_record.controller.vo.BaseVO;
import com.reward_project.play_record.entity.PrizeRecord;
import com.reward_project.play_record.integration.SendCoinIntegration;
import com.reward_project.play_record.integration.cmd.SendCoinCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("coin")
public class CoinPrizeHandler extends AbstractPrizeHandler{

    @Autowired
    private SendCoinIntegration sendCoinIntegration;

    @Override
    public BaseVO send(PrizeRecord prizeRecord) {
        SendCoinCmd sendCoinCmd = buildSendCoinCmd(prizeRecord);
        return sendCoinIntegration.sendCoinIntegration(sendCoinCmd);
    }


    private static SendCoinCmd buildSendCoinCmd(PrizeRecord prizeRecord) {
        SendCoinCmd sendCoinCmd = new SendCoinCmd();
        sendCoinCmd.setUserId(prizeRecord.getUserId());
        sendCoinCmd.setTheme(prizeRecord.getTheme());
        sendCoinCmd.setPrizeAmount(prizeRecord.getPrizeAmount());
        sendCoinCmd.setPrizeType(prizeRecord.getPrizeType());
        sendCoinCmd.setPrizeSubType(prizeRecord.getPrizeSubType());
        sendCoinCmd.setOutBizNo(prizeRecord.getOutBizNo());
        return sendCoinCmd;
    }

}
