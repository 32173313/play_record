package com.reward_project.play_record.controller;

import com.reward_project.play_record.controller.cmd.PlayRecordCmd;
import com.reward_project.play_record.controller.vo.BaseVO;
import com.reward_project.play_record.service.SyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/music/play")
public class SyncController {
    @Autowired
    private SyncService syncService;

    @PostMapping("/sync")
    public BaseVO syncProcess(@RequestBody PlayRecordCmd playRecordCmd) {
        long start = System.currentTimeMillis();
        long end;
        try {
            syncService.syncProcess(playRecordCmd);
            end = System.currentTimeMillis();
            return BaseVO.buildBaseVO(200, end - start, true, null);
        } catch (Exception e) {
            end = System.currentTimeMillis();
            return BaseVO.buildBaseVO(500, end - start, false, "同步播放记录失败，未知异常");
        }
    }
}
