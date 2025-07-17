package com.jigubangbang.quest_service.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRankingDto {
    private String user_id;
    private String nickname;
    private String profile_image;
    private int level;
    private int xp;
    private int completed_quest;
    private int in_progress_quest;
    private int rank;
}
