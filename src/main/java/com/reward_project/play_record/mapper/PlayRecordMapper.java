package com.reward_project.play_record.mapper;

import com.reward_project.play_record.entity.PlayRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface PlayRecordMapper {
    void add(PlayRecord playRecord);
    List<PlayRecord> queryPlayRecordByUserIdAndSyncTimeAndType(int userId, Date startTime, Date endTime, String type, String subType);
}
