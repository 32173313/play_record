package com.reward_project.play_record.integration.cmd;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SendCoinCmd {
    private int userId;
    private String theme;
    private String prizeType;
    private String prizeSubType;
    private int prizeAmount;
    private String outBizNo;

}
