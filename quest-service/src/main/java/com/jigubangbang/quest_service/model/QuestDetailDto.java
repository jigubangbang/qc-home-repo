package com.jigubangbang.quest_service.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestDetailDto {
    private int quest_user_id;
    
    // quest_user에서
    private String user_id;
    private int quest_id;
    private String status; // ENUM('IN_PROGRESS', 'COMPLETED', 'PENDING','GIVEN_UP')
    private Timestamp started_at;
    private Timestamp completed_at;
    private Timestamp given_up_at;
    
    // quest에서
    private String title;
    private String difficulty; // ENUM('EASY', 'MEDIUM', 'HARD')
    private int xp;
    private String description;
    private int category;
    private Boolean is_seasonal;
    private Timestamp season_start;
    private Timestamp season_end;
}
