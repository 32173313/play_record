package com.reward_project.play_record.service.impl;

import com.reward_project.play_record.entity.PlayRecordSummary;
import com.reward_project.play_record.exception.PlayRecordSummaryInsertException;
import com.reward_project.play_record.mapper.PlayRecordSummaryMapper;
import com.reward_project.play_record.service.PlayRecordSummaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
public class PlayRecordSummaryServiceImpl implements PlayRecordSummaryService {

    @Autowired
    private PlayRecordSummaryMapper playRecordSummaryMapper;

    Logger logger = LoggerFactory.getLogger(PlayRecordSummaryServiceImpl.class);

    // 给sync接口 在多线程情况下写（加锁）
    @Override
    public PlayRecordSummary queryPlayRecordSummaryForUpdate(int userId, String currentDate, String type, String subType) {
        return playRecordSummaryMapper.queryByUserIdAndDateAndTypeForUpdate(userId, currentDate, type, subType);
    }

    // 给客服 未加锁 客服查询不改写
    @Override
    public PlayRecordSummary queryPlayRecordSummary(int userId, String currentDate, String type, String subType) {
        return playRecordSummaryMapper.queryByUserIdAndDateAndType(userId, currentDate, type, subType);
    }

    @Override
    public void addPlayRecordSummary(PlayRecordSummary playRecordSummary) {
        // 数据库唯一键可能会发送异常 为了保证接口可用 需要在service将异常catch
        try {
            playRecordSummaryMapper.addPlayRecordSummary(playRecordSummary);
        } catch (DuplicateKeyException e) { // 唯一键是避免重复，有重复是正常的，数据库层面做了处理，可以忽略
            logger.error(String.format("用户%d在%s的播放总记录已存在，无需重复插入", playRecordSummary.getUserId(), playRecordSummary.getCurrentDate()));
        } catch (Exception e) {
            logger.error(String.format("用户%d在%s的播放总记录插入失败，未知异常", playRecordSummary.getUserId(), playRecordSummary.getCurrentDate()));
            throw new PlayRecordSummaryInsertException(String.format("用户%d在%s的播放总记录插入失败，未知异常", playRecordSummary.getUserId(), playRecordSummary.getCurrentDate()));
        }
    }

    @Override
    public void updatePlayRecordSummary(PlayRecordSummary playRecordSummary) {
        try {
            playRecordSummaryMapper.updatePlayRecordSummary(playRecordSummary);
        } catch (Exception e) {
            logger.error(String.format("用户%d在%s的播放总时长更新失败", playRecordSummary.getUserId(), playRecordSummary.getCurrentDate()));
        }

    }

}
