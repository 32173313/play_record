package com.reward_project.play_record.mapper;

import com.reward_project.play_record.entity.RewardRule;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RewardRuleMapper {
    RewardRule queryById(int id);
    RewardRule queryByName(String name);
    void addRule(RewardRule rewardRule);
    void updateRule(RewardRule rewardRule);
}
