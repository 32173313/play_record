package com.reward_project.play_record.controller.converter;

import com.reward_project.play_record.controller.vo.PlayRecordVO;
import com.reward_project.play_record.entity.PlayRecord;

import java.util.ArrayList;
import java.util.List;

public class PlayRecordVOConverter {
    public static PlayRecordVO convertToVO(PlayRecord playRecord) {
        PlayRecordVO playRecordVO = new PlayRecordVO();
        playRecordVO.setUserId(playRecord.getUserId());
        playRecordVO.setMusicId(playRecord.getMusicId());
        playRecordVO.setDuration(playRecord.getDuration());
        playRecordVO.setSyncTime(playRecord.getSyncTime());
        return playRecordVO;
    }

    public static List<PlayRecordVO> convertToList(List<PlayRecord> playRecords) {
        List<PlayRecordVO> playRecordVOS = new ArrayList<>();
        for (PlayRecord playRecord : playRecords) {
            playRecordVOS.add(convertToVO(playRecord));
        }
        return playRecordVOS;
    }
}
