package com.reward_project.play_record.util;

import com.reward_project.play_record.constant.RuleConstant;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
class RuleMatchUtilTest {

    @Test
    void buildPrizeStageInfo() {
        HashMap<String, Long> ruleMatchMap = new HashMap<>();
        ruleMatchMap.put("totalDuration", 180L);
        ruleMatchMap.put("prizeAmount", 0L);
        ruleMatchMap.put("stage", 0L);
        int stage = (int) RuleMatchUtil.buildPrizeStageInfol(RuleConstant.STAGE_RULE, ruleMatchMap);
        int prizeAmount = (int) RuleMatchUtil.buildPrizeStageInfol(RuleConstant.PRIZE_AMOUNT_RULE, ruleMatchMap);
        Assert.assertEquals(1, stage);
        Assert.assertEquals(1, prizeAmount);
    }

}