package com.jigubangbang.quest_service.model;

import java.sql.Timestamp;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuestCerti {
    private int id;
    private String user_id;
    private int quest_id;
    private String quest_description;
    private String status; //ENUM('IN_PROGRESS', 'COMPLETED', 'GIVEN_UP')
    private Timestamp started_at;
    private Timestamp completed_at;
    private Timestamp given_up_at;

    private String title;
    private String description; //test
    private String difficulty; // ENUM('EASY', 'MEDIUM', 'HARD')
    private int xp;
    private List<String> image_list;
}
