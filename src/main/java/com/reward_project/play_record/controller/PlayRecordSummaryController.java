package com.reward_project.play_record.controller;

import com.reward_project.play_record.controller.converter.PlayRecordSummaryVOConverter;
import com.reward_project.play_record.controller.vo.BaseVO;
import com.reward_project.play_record.controller.vo.PlayRecordSummaryPageVO;
import com.reward_project.play_record.entity.PlayRecordSummary;
import com.reward_project.play_record.service.PlayRecordSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/play/record/summary")
public class PlayRecordSummaryController {

    @Autowired
    private PlayRecordSummaryService playRecordSummaryService;

    // 给客服的查询播放汇总的接口
    @GetMapping("/get")
    public PlayRecordSummaryPageVO queryPlayRecordSummary(int userId, String currentDate, String type, String subType) {
        long start = System.currentTimeMillis();
        long end;
        PlayRecordSummaryPageVO playRecordSummaryPageVO = new PlayRecordSummaryPageVO();
        try {
            // 给客服的查询接口
            PlayRecordSummary playRecordSummary = playRecordSummaryService.queryPlayRecordSummary(userId, currentDate, type, subType);
            playRecordSummaryPageVO.setPlayRecordSummaryVO(PlayRecordSummaryVOConverter.convertToVO(playRecordSummary));
            end = System.currentTimeMillis();
            playRecordSummaryPageVO.setBaseVO(BaseVO.buildBaseVO(200, end - start, true, null));
            return playRecordSummaryPageVO;
        } catch (Exception e) {
            end = System.currentTimeMillis();
            playRecordSummaryPageVO.setBaseVO(BaseVO.buildBaseVO(500, end - start, true, null));
            return playRecordSummaryPageVO;
        }
    }
}
