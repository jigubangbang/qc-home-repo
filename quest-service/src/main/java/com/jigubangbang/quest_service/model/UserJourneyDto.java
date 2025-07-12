package com.jigubangbang.quest_service.model;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserJourneyDto {
    private String user_id;
    private String profile_image;
    private String nickname;
    private int completed_quest_count;
    private int in_progress_quest_count;
    private int level;
    private int xp;
}
