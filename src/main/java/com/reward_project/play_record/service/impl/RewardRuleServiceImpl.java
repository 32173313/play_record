package com.reward_project.play_record.service.impl;

import com.reward_project.play_record.entity.RewardRule;
import com.reward_project.play_record.exception.RewardRuleNotExistException;
import com.reward_project.play_record.mapper.RewardRuleMapper;
import com.reward_project.play_record.repository.RewardRuleRepository;
import com.reward_project.play_record.service.RewardRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RewardRuleServiceImpl implements RewardRuleService {

    Logger logger = LoggerFactory.getLogger(RewardRuleServiceImpl.class);

    @Autowired
    private RewardRuleMapper rewardRuleMapper;

    @Autowired
    private RewardRuleRepository rewardRuleRepository;

    @Override
    @Transactional
    // 客服异常 如果catch异常则为一个自定义异常 没必要
    public void addRule(RewardRule rewardRule) {
        rewardRuleMapper.addRule(rewardRule);
        rewardRuleRepository.addRule(rewardRule);
    }

    @Override
    @Transactional
    public void updateRule(RewardRule rewardRule) {
        RewardRule rewardRule1 = queryById(rewardRule.getId());
        if (rewardRule1 == null) {
            logger.error(String.format("要更改的发奖规则不存在，id为%d，名字为%s", rewardRule.getId(), rewardRule.getName()));
            throw new RewardRuleNotExistException(String.format("要更改的发奖规则不存在，id为%d，名字为%s", rewardRule.getId(), rewardRule.getName()));
        }
        rewardRule1.setRule(rewardRule.getRule());
        rewardRule1.setStatus(rewardRule.getStatus());
        rewardRuleMapper.updateRule(rewardRule1);
        rewardRuleRepository.deleteRule(rewardRule1.getName());
    }

    @Override
    public RewardRule queryById(int id) {
        return rewardRuleMapper.queryById(id);
    }

    @Override
    public RewardRule queryByName(String name) {
        RewardRule rewardRule = rewardRuleRepository.getRule(name);
        if (rewardRule != null) {
            return rewardRule;
        } else {
            RewardRule rewardRule1 = rewardRuleMapper.queryByName(name);
            rewardRuleRepository.addRule(rewardRule1);
            return rewardRule1;
        }
    }
}
