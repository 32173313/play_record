package com.reward_project.play_record.controller;

import com.reward_project.play_record.controller.converter.PrizeRecordVOConverter;
import com.reward_project.play_record.controller.vo.BaseVO;
import com.reward_project.play_record.controller.vo.PrizeRecordPageVO;
import com.reward_project.play_record.controller.vo.PrizeRecordVO;
import com.reward_project.play_record.entity.PrizeRecord;
import com.reward_project.play_record.service.PrizeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/prize/record")
public class PrizeRecordController {

    @Autowired
    private PrizeRecordService prizeRecordService;

    @GetMapping("/get")
    public PrizeRecordPageVO getPrizeRecord(int userId, String date, String prizeType, String prizeSubType) {
        long start = System.currentTimeMillis();
        long end;
        PrizeRecordPageVO prizeRecordPageVO = new PrizeRecordPageVO();
        try {
            List<PrizeRecord> prizeRecords = prizeRecordService.queryPrizeRecord(userId, date, prizeType, prizeSubType);
            List<PrizeRecordVO> prizeRecordVOList = PrizeRecordVOConverter.convertToList(prizeRecords);
            prizeRecordPageVO.setPrizeRecordVO(prizeRecordVOList);
            end = System.currentTimeMillis();
            prizeRecordPageVO.setBaseVO(BaseVO.buildBaseVO(200, end - start, true, null));
            return prizeRecordPageVO;
        } catch (Exception e) {
            end = System.currentTimeMillis();
            prizeRecordPageVO.setBaseVO(BaseVO.buildBaseVO(500, end - start, false, "other exception"));
            return prizeRecordPageVO;
        }
    }
}
