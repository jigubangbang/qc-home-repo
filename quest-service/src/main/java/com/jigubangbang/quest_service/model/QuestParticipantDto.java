package com.jigubangbang.quest_service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestParticipantDto {
    private String user_id;
    private String name;
    private String nickname;

    private String profile_image;
    private Boolean is_premium;
    private String user_status;
    private String bio;
    private int xp;
    private int level;

    //join
    private int quest_completed;
    private int quest_in_progress;
}
