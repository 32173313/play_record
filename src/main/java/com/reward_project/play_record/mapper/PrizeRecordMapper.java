package com.reward_project.play_record.mapper;

import com.reward_project.play_record.entity.PrizeRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PrizeRecordMapper {
    void addPrizeRecord(PrizeRecord prizeRecord);
    // 记录插入之后不会重写 和用户播放记录不一样 只需要防止插入相同记录 （幂等）
    List<PrizeRecord> queryByUserIdAndDateAndType(int userId, String date, String prizeType, String prizeSubType);
}
