package com.reward_project.play_record.entity;

import lombok.*;
import org.springframework.stereotype.Service;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PrizeRecord {
    // 用户在哪一天在什么场景 在哪个阶段 发放什么奖励
    private int id;
    private int userId;
    // 精确到天维度
    private String date;
    private int stage;
    private String theme;
    // 发奖类型=coin code
    private String prizeType;
    private String prizeSubType;
    private int prizeAmount;
    // 外部业务号：为了让下游幂等
    private String outBizNo;
    private Date createTime;
    private Date updateTime;
}
