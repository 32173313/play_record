package com.reward_project.play_record.controller.vo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RewardRuleVO {
    private int id;
    private String name;
    private String rule;
    private String status;
}
