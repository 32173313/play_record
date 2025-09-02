package com.reward_project.play_record.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 枚举一定要加 构造函数 和 getter ！！！！！！！！！！！
@Getter
@AllArgsConstructor
public enum DateFormatPatternEnum {

    DATE_FORMAT_PATTERN_ENUM("yyyy-MM-dd", "日维度");

    private String code;
    private String desc;
}
