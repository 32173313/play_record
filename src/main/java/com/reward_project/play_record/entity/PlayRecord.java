package com.reward_project.play_record.entity;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PlayRecord {
    private int id;
    private int userId;
    private int musicId;
    private long duration;
    private String type;
    private String subType;
    private Date syncTime;
    private Date createTime;
    private Date updateTime;
}
