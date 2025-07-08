package com.jigubangbang.quest_service.model;

import java.sql.Timestamp;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdminQuestDetailDto {
    private int quest_id;   
    private String type; //auth/check 

    //quest_category table에서 join
    private String category;

    private String title;
    private String difficulty; 
    private int xp;
    private Boolean is_seasonal;
    private Timestamp season_start;
    private Timestamp season_end;
    private String status;       //종료 됐는지 아닌지 active / inactive
    private Timestamp created_at;

    private String description;

    private int count_completed;
    private int count_in_progress;
    private int count_given_up;
}
