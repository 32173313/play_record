package com.reward_project.play_record.mapper;

import com.reward_project.play_record.entity.PlayRecordSummary;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PlayRecordSummaryMapper {
    void addPlayRecordSummary(PlayRecordSummary playRecordSummary);
    void updatePlayRecordSummary(PlayRecordSummary playRecordSummary);
    // 这个是给客服的辅助接口
    PlayRecordSummary queryByUserIdAndDateAndType(int userId, String currentDate, String type, String subType);
    // 这里加了一个写锁，这个接口会被大量调用，所以客服用的是另一个接口
    PlayRecordSummary queryByUserIdAndDateAndTypeForUpdate(int userId, String currentDate, String type, String subType);
}
