package com.reward_project.play_record.controller.vo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PlayRecordSummaryVO {
    private int id;
    private int userId;
    private long totalDuration;
    private String currentDate;
    private Date lastPlayTime;
    private Date createTime;
    private Date updateTime;
}
