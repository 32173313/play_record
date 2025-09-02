package com.reward_project.play_record.repository;

import com.reward_project.play_record.entity.RewardRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RewardRuleRepository {

    @Autowired
    private RedisTemplate redisTemplate;

    public void addRule(RewardRule rewardRule) {
        redisTemplate.opsForValue().set(buildKey(rewardRule.getName()), rewardRule);
    }

    public RewardRule getRule(String name) {
        return (RewardRule) redisTemplate.opsForValue().get(buildKey(name));
    }

    public void deleteRule(String name) {
        redisTemplate.delete(buildKey(name));
    }

    public String buildKey(String name) {
        return "rewardRule:" + name;
    }
}
