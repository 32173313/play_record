package com.reward_project.play_record.service;

import com.reward_project.play_record.controller.cmd.PlayRecordCmd;
import com.reward_project.play_record.entity.PlayRecord;
import com.reward_project.play_record.entity.PrizeRecord;

public interface SyncService {
    void syncProcess(PlayRecordCmd playRecordCmd);
    public void sendCoinAndAddPrizeRecord(PrizeRecord prizeRecord);
}
