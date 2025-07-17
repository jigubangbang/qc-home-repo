package com.jigubangbang.quest_service.model;

import java.sql.Timestamp;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdminBadgeDetailDto {
    private int badge_id;   //badge table의 id
    private String kor_title;
    private String eng_title;
    private String description;
    private String icon;
    private Timestamp created_at;
    private int difficulty;

    private List<String> quest; //quest table에서 title만 string으로
    private int count_awarded;
}