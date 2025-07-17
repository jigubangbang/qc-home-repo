package com.jigubangbang.quest_service.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestUserDto {
    private int id;
    private String user_id;
    private int quest_id;
    private String status; //ENUM('IN_PROGRESS', 'COMPLETED', 'PENDING','GIVEN_UP')
    private Timestamp started_at;
    private Timestamp completed_at;
    private Timestamp given_up_at;

    private String title;
    private String difficulty; // ENUM('EASY', 'MEDIUM', 'HARD')
    private int xp;

    private String description;
    private String icon;
    private String badge;
    private int progress;
    
    private String quest_status;
}