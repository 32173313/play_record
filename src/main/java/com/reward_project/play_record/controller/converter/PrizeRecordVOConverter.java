package com.reward_project.play_record.controller.converter;

import com.reward_project.play_record.controller.vo.PrizeRecordVO;
import com.reward_project.play_record.entity.PrizeRecord;

import java.util.ArrayList;
import java.util.List;

public class PrizeRecordVOConverter {
    public static PrizeRecordVO convertToVO(PrizeRecord prizeRecord) {
        PrizeRecordVO prizeRecordVO = new PrizeRecordVO();
        prizeRecordVO.setId(prizeRecord.getId());
        prizeRecordVO.setUserId(prizeRecord.getUserId());
        prizeRecordVO.setDate(prizeRecord.getDate());
        prizeRecordVO.setStage(prizeRecord.getStage());
        prizeRecordVO.setTheme(prizeRecord.getTheme());
        prizeRecordVO.setPrizeType(prizeRecord.getPrizeType());
        prizeRecordVO.setPrizeAmount(prizeRecord.getPrizeAmount());
        prizeRecordVO.setCreateTime(prizeRecord.getCreateTime());
        prizeRecordVO.setUpdateTime(prizeRecord.getUpdateTime());
        return prizeRecordVO;
    }

    public static List<PrizeRecordVO> convertToList(List<PrizeRecord> prizeRecordList) {
        List<PrizeRecordVO> prizeRecordVOList = new ArrayList<>();
        for(PrizeRecord prizeRecord : prizeRecordList) {
            PrizeRecordVO prizeRecordVO = convertToVO(prizeRecord);
            prizeRecordVOList.add(prizeRecordVO);
        }
        return prizeRecordVOList;
    }
}
