package com.reward_project.play_record.controller;


import com.reward_project.play_record.controller.converter.PlayRecordVOConverter;
import com.reward_project.play_record.controller.vo.BaseVO;
import com.reward_project.play_record.controller.vo.PlayRecordPageVO;
import com.reward_project.play_record.controller.vo.PlayRecordVO;
import com.reward_project.play_record.entity.PlayRecord;
import com.reward_project.play_record.service.PlayRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/play/record")
public class PlayRecordController {

    @Autowired
    private PlayRecordService playRecordService;

    @GetMapping("/get")
    public PlayRecordPageVO getPlayRecordByDate(int userId, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS") Date startTime, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS") Date endTime, String type, String subType) {
        long start = System.currentTimeMillis();
        long end;
        PlayRecordPageVO playRecordPageVO = new PlayRecordPageVO();
        try {
            List<PlayRecord> playRecords = playRecordService.queryPlayRecordByUserIdAndSyncTimeAndType(userId, startTime, endTime, type, subType);
            List<PlayRecordVO> playRecordVOList = PlayRecordVOConverter.convertToList(playRecords);
            playRecordPageVO.setPlayRecordVOList(playRecordVOList);
            end = System.currentTimeMillis();
            playRecordPageVO.setBaseVO(BaseVO.buildBaseVO(200, end - start, true, null));
            return playRecordPageVO;
        } catch (Exception e) {
            end = System.currentTimeMillis();
            playRecordPageVO.setBaseVO(BaseVO.buildBaseVO(500, end - start, false, "获取播放明细失败，未知异常"));
            return playRecordPageVO;
        }


    }
}
