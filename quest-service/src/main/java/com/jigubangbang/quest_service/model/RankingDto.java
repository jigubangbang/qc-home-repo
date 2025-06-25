package com.jigubangbang.quest_service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RankingDto {
    private String user_id;
    private String nickname;
    private String profile_image;
    private int level;

    private int weekly_quest_count;
    private int weekly_level_gain;
    private int total_quest_count;
}
