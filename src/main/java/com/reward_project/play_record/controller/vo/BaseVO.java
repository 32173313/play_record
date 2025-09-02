package com.reward_project.play_record.controller.vo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BaseVO {
    private int code;
    private long time;
    private boolean success;
    private String errorMsg;

    public static BaseVO buildBaseVO(int code, long time, boolean success, String errorMsg) {
        return new BaseVO(code, time, success, errorMsg);
    }
}

