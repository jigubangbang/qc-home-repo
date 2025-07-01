package com.jigubangbang.quest_service.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuestDto {
    private int id;
    private String type;         // ENUM('AUTH', 'CHECK')
    private int category;
    private String title;
    private String difficulty;  // ENUM('EASY', 'MEDIUM', 'HARD')
    private int xp;
    private Boolean is_seasonal;
    private Timestamp season_start;
    private Timestamp season_end;
    private String status;       // ENUM( 'AUTH', 'CHECK' )
    private Timestamp created_at;
    
    private String icon;

    //join
    private int count_in_progress;
    private int count_completed;
}
