package com.reward_project.play_record.controller.cmd;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PlayRecordCmd {
    private int userId;
    private int musicId;
    private long duration;
    private String theme;
    private String prizeType;
    private String prizeSubType;
}
