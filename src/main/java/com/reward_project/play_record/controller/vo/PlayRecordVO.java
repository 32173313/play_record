package com.reward_project.play_record.controller.vo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PlayRecordVO {
    private int userId;
    private int musicId;
    private long duration;
    private Date syncTime;
}
