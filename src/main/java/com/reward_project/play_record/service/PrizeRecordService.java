package com.reward_project.play_record.service;

import com.reward_project.play_record.entity.PrizeRecord;

import java.util.List;

public interface PrizeRecordService {
    List<PrizeRecord> queryPrizeRecord(int userId, String date, String prizeType, String prizeSubType);
    void addPrizeRecord(PrizeRecord prizeRecord);
}
