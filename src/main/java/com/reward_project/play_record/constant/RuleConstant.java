package com.reward_project.play_record.constant;

public class RuleConstant {
    public static final String STAGE_RULE =
            "  stage = \n" +
                    "    totalDuration < 160  ? 0  :\n" +
                    "    totalDuration < 320  ? 1  :\n" +
                    "    totalDuration < 640  ? 2  :\n" +
                    "    totalDuration < 960  ? 3  :\n" +
                    "    totalDuration < 1280 ? 4  :\n" +
                    "    totalDuration < 1600 ? 5  :\n" +
                    "    totalDuration < 3200 ? 6  : 7 \n " ;


    public static final String PRIZE_AMOUNT_RULE =
            "  prizeAmount = \n" +
                    "    totalDuration < 160  ? 0  :\n" +
                    "    totalDuration < 320  ? 1  :\n" +
                    "    totalDuration < 640  ? 1  :\n" +
                    "    totalDuration < 960  ? 2  :\n" +
                    "    totalDuration < 1280 ? 2  :\n" +
                    "    totalDuration < 1600 ? 2  :\n" +
                    "    totalDuration < 3200 ? 10 : 10 \n " ;
}
