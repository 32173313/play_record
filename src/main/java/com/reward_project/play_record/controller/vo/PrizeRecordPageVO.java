package com.reward_project.play_record.controller.vo;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PrizeRecordPageVO {
    private List<PrizeRecordVO> prizeRecordVO;
    private BaseVO baseVO;
}
