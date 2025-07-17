package com.jigubangbang.quest_service.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SimpleBadgeDto {
    private int id;
    private int quest_id;
    private int badge_id;
    private String icon;
    private String kor_title;
    private String eng_title;
}
