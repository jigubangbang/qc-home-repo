package com.jigubangbang.quest_service.model;

import java.sql.Timestamp;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserBadgeDto {
    private int id;
    private String user_id;
    private int badge_id;
    private String kor_title;
    private String eng_title;
    private String description;
    private String icon;
    private String difficulty;
    private Timestamp created_at;

    private Boolean is_awarded;
    private Timestamp awarded_at;
    private Boolean is_displayed;
    private Timestamp pinned_at;

    private List<BadgeQuestDto> quests;
    private int completed_quest;
    private int total_quest;

}
