package com.reward_project.play_record.controller.converter;

import com.reward_project.play_record.controller.vo.RewardRuleVO;
import com.reward_project.play_record.entity.RewardRule;

public class RewardRuleVOConverter {

    public static RewardRuleVO convertToVO(RewardRule rewardRule) {
        RewardRuleVO rewardRuleVO = new RewardRuleVO();
        rewardRuleVO.setId(rewardRule.getId());
        rewardRuleVO.setName(rewardRule.getName());
        rewardRuleVO.setRule(rewardRule.getRule());
        rewardRuleVO.setStatus(rewardRule.getStatus());
        return rewardRuleVO;
    }
}
