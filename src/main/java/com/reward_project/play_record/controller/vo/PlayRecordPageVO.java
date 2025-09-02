package com.reward_project.play_record.controller.vo;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PlayRecordPageVO {
    private List<PlayRecordVO> playRecordVOList;
    private BaseVO baseVO;
}
