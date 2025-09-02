package com.reward_project.play_record.controller.vo;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PrizeRecordVO {
    private int id;
    private int userId;
    private String date;
    private int stage;
    private String theme;
    private String prizeType;
    private int prizeAmount;
    private Date createTime;
    private Date updateTime;
}
