package com.reward_project.play_record.service;

import com.reward_project.play_record.entity.PlayRecordSummary;

public interface PlayRecordSummaryService {
    PlayRecordSummary queryPlayRecordSummaryForUpdate(int userId, String currentDate, String type, String subType);
    PlayRecordSummary queryPlayRecordSummary(int userId, String currentDate, String type, String subType);
    void addPlayRecordSummary(PlayRecordSummary playRecordSummary);
    void updatePlayRecordSummary(PlayRecordSummary playRecordSummary);
}
