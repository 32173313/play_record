package com.reward_project.play_record.entity;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RewardRule {
    private int id;
    private String name;
    private String rule;
    private String status;
    private Date createTime;
    private Date updateTime;
}
