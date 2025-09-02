package com.reward_project.play_record.service.impl;

import com.reward_project.play_record.entity.PrizeRecord;
import com.reward_project.play_record.mapper.PrizeRecordMapper;
import com.reward_project.play_record.service.PrizeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrizeRecordServiceImpl implements PrizeRecordService {

    @Autowired
    private PrizeRecordMapper prizeRecordMapper;

    @Override
    public List<PrizeRecord> queryPrizeRecord(int userId, String date, String prizeType, String prizeSubType) {
        // 不用加缓存 加缓存是因为大量用户在查询 压力大 而发奖记录只有客户看
        return prizeRecordMapper.queryByUserIdAndDateAndType(userId, date, prizeType, prizeSubType);
    }

    @Override
    public void addPrizeRecord(PrizeRecord prizeRecord) {
        prizeRecordMapper.addPrizeRecord(prizeRecord);
    }
}
