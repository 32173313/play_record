package com.reward_project.play_record.entity;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PlayRecordSummary {
    private int id;
    private int userId;
    private long totalDuration;
    private String type;
    private String subType;
    private String currentDate;
    private Date lastPlayTime;
    private Date createTime;
    private Date updateTime;
}
