package com.jigubangbang.quest_service.model;

import java.sql.Timestamp;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BadgeDto {
    private int id;
    private String kor_title;
    private String eng_title;
    private String description;
    private String icon;
    private Timestamp created_at;
    private int difficulty; //쉬움 1 보통 2 어려움 3 시즌 4

    private List<String> quest;
    private int acquired_count;
}
