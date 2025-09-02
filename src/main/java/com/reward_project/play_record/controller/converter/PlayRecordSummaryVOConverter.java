package com.reward_project.play_record.controller.converter;

import com.reward_project.play_record.controller.vo.PlayRecordSummaryVO;
import com.reward_project.play_record.entity.PlayRecordSummary;

public class PlayRecordSummaryVOConverter {
    public static PlayRecordSummaryVO convertToVO(PlayRecordSummary playRecordSummary) {
        PlayRecordSummaryVO playRecordSummaryVO = new PlayRecordSummaryVO();
        playRecordSummaryVO.setId(playRecordSummary.getId());
        playRecordSummaryVO.setUserId(playRecordSummary.getUserId());
        playRecordSummaryVO.setTotalDuration(playRecordSummary.getTotalDuration());
        playRecordSummaryVO.setCurrentDate(playRecordSummary.getCurrentDate());
        playRecordSummaryVO.setLastPlayTime(playRecordSummary.getLastPlayTime());
        playRecordSummaryVO.setCreateTime(playRecordSummary.getCreateTime());
        playRecordSummaryVO.setUpdateTime(playRecordSummary.getUpdateTime());
        return playRecordSummaryVO;
    }
}
