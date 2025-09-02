package com.reward_project.play_record.controller;

import com.reward_project.play_record.controller.converter.RewardRuleVOConverter;
import com.reward_project.play_record.controller.vo.BaseVO;
import com.reward_project.play_record.controller.vo.RewardRulePageVO;
import com.reward_project.play_record.controller.vo.RewardRuleVO;
import com.reward_project.play_record.entity.RewardRule;
import com.reward_project.play_record.service.RewardRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reward/rule")
public class RewardRuleController {
    @Autowired
    private RewardRuleService rewardRuleService;

    @PostMapping("/add")
    private BaseVO addRule(@RequestBody RewardRule rewardRule) {
        long start = System.currentTimeMillis();
        long end;
        try {
            rewardRuleService.addRule(rewardRule);
            end = System.currentTimeMillis();
            return BaseVO.buildBaseVO(200, end - start, true, null);
        } catch (Exception e) {
            end = System.currentTimeMillis();
            return BaseVO.buildBaseVO(500, end - start, false, "添加发奖规则失败，未知异常");
        }
    }

    @PutMapping("/update")
    private BaseVO updateRule(@RequestBody RewardRule rewardRule) {
        long start = System.currentTimeMillis();
        long end;
        try {
            rewardRuleService.updateRule(rewardRule);
            end = System.currentTimeMillis();
            return BaseVO.buildBaseVO(200, end - start, true, null);
        } catch (Exception e) {
            end = System.currentTimeMillis();
            return BaseVO.buildBaseVO(500, end - start, false, "更新发奖规则失败，未知异常");
        }
    }

    @DeleteMapping("/delete")
    private BaseVO deleteRule(@RequestBody RewardRule rewardRule) {
        long start = System.currentTimeMillis();
        long end;
        try {
            rewardRuleService.updateRule(rewardRule);
            end = System.currentTimeMillis();
            return BaseVO.buildBaseVO(200, end - start, true, null);
        } catch (Exception e) {
            end = System.currentTimeMillis();
            return BaseVO.buildBaseVO(500, end - start, false, "下架发奖规则失败，未知异常");
        }
    }

    @GetMapping("/get/by/id")
    public RewardRulePageVO getRuleById(int id) {
        long start = System.currentTimeMillis();
        long end;
        RewardRulePageVO rewardRulePageVO = new RewardRulePageVO();
        try {
            RewardRule rewardRule = rewardRuleService.queryById(id);
            RewardRuleVO rewardRuleVO = RewardRuleVOConverter.convertToVO(rewardRule);
            rewardRulePageVO.setRewardRuleVO(rewardRuleVO);
            end = System.currentTimeMillis();
            rewardRulePageVO.setBaseVO(BaseVO.buildBaseVO(200, end - start, true, null));
            return rewardRulePageVO;
        } catch (Exception e) {
            end = System.currentTimeMillis();
            rewardRulePageVO.setBaseVO(BaseVO.buildBaseVO(500, end - start, false, "获取发奖规则失败，未知异常"));
            return rewardRulePageVO;
        }
    }

    @GetMapping("get/by/name")
    public RewardRulePageVO getRuleByName(String name) {
        long start = System.currentTimeMillis();
        long end;
        RewardRulePageVO rewardRulePageVO = new RewardRulePageVO();
        try {
            RewardRule rewardRule = rewardRuleService.queryByName(name);
            RewardRuleVO rewardRuleVO = RewardRuleVOConverter.convertToVO(rewardRule);
            rewardRulePageVO.setRewardRuleVO(rewardRuleVO);
            end = System.currentTimeMillis();
            rewardRulePageVO.setBaseVO(BaseVO.buildBaseVO(200, end - start, true, null));
            return rewardRulePageVO;
        } catch (Exception e) {
            end = System.currentTimeMillis();
            rewardRulePageVO.setBaseVO(BaseVO.buildBaseVO(500, end - start, false, "获取发奖规则失败，未知异常"));
            return rewardRulePageVO;
        }
    }
}
