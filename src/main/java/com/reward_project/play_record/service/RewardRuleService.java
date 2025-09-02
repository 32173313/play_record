package com.reward_project.play_record.service;

import com.reward_project.play_record.entity.RewardRule;

public interface RewardRuleService {
    void addRule(RewardRule rewardRule);
    void updateRule(RewardRule rewardRule);
    RewardRule queryById(int id);
    RewardRule queryByName(String name);
}
