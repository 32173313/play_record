package com.reward_project.play_record.service.impl;

import com.reward_project.play_record.controller.cmd.PlayRecordCmd;
import com.reward_project.play_record.entity.PlayRecord;
import com.reward_project.play_record.exception.DurationIllegalException;
import com.reward_project.play_record.mapper.PlayRecordMapper;
import com.reward_project.play_record.service.PlayRecordService;
import com.reward_project.play_record.util.TimeConverterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PlayRecordServiceImpl implements PlayRecordService {

    @Autowired
    private PlayRecordMapper playRecordMapper;

    Logger logger = LoggerFactory.getLogger(PlayRecordServiceImpl.class);

    @Override
    public void addPlayRecord(PlayRecordCmd playRecordCmd) {
        PlayRecord playRecord1 = new PlayRecord();
        playRecord1.setUserId(playRecordCmd.getUserId());
        playRecord1.setMusicId(playRecordCmd.getMusicId());
        // 上游已经检查了music存在及完整
        // 此处需要唯一键加在user_id, music_id, sync_time上，防止在网络比较差的情况下触发重传（基于TCP协议），记录了多条，发奖发多
        // 校验duration的合法性，需要小于等于30s（防止用户改时间）

        if (playRecordCmd.getDuration() > 30L) {
            // 此处打日志，记录违规用户
            // 程序不会报错，但可能为空时才用warn
            logger.error(String.format("%d用户上报时长不合法", playRecordCmd.getUserId()));
            throw new DurationIllegalException(String.format("%d用户上报时长不合法", playRecordCmd.getUserId()));
        }
        playRecord1.setDuration(playRecordCmd.getDuration());
        playRecord1.setType(playRecordCmd.getPrizeType());
        playRecord1.setSubType(playRecordCmd.getPrizeSubType());
        try {
            playRecordMapper.add(playRecord1);
        } catch (Exception e) {
            logger.error(String.format("%d用户播放%d的记录插入失败", playRecordCmd.getUserId(), playRecordCmd.getMusicId()));
        }
    }

    @Override
    public List<PlayRecord> queryPlayRecordByUserIdAndSyncTimeAndType(int userId, Date startTime, Date endTime, String type, String subType) {
        return playRecordMapper.queryPlayRecordByUserIdAndSyncTimeAndType(userId, startTime, endTime, type, subType);
    }

    @Override
    public long totalDurationByDay(int userId, String type, String subType) {
        List<PlayRecord> playRecords = queryPlayRecordByUserIdAndSyncTimeAndType(userId, TimeConverterUtil.setTime(0, 0, 0), TimeConverterUtil.setTime(23, 59, 59), type, subType);
        long totalDuration = 0L;
        for (PlayRecord playRecord : playRecords) {
            totalDuration += playRecord.getDuration();
        }
        return totalDuration;
    }
}
