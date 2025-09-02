package com.reward_project.play_record.service;

import com.reward_project.play_record.controller.cmd.PlayRecordCmd;
import com.reward_project.play_record.entity.PlayRecord;

import java.util.Date;
import java.util.List;

public interface PlayRecordService {
    void addPlayRecord(PlayRecordCmd playRecordCmd);
    List<PlayRecord> queryPlayRecordByUserIdAndSyncTimeAndType(int userId, Date startTime, Date endTime, String type, String subType);
    long totalDurationByDay(int userId, String type, String subType);

}
