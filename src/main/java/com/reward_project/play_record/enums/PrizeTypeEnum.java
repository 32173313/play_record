package com.reward_project.play_record.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 枚举一定要加 构造函数 和 getter ！！！！！！！！！！！
@Getter
@AllArgsConstructor
public enum PrizeTypeEnum {
    COIN("coin", "金币奖励"),
    CREDIT("credit", "积分奖励"),
    RED_POCKET("red_pocket", "红包奖励");

    private String code;
    private String desc;
}
