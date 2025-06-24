package com.jigubangbang.quest_service.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BadgeQuestDto {
    private int id;
    private int badge_id;
    private int quest_id;
    private BadgeDto badge;
    private QuestDto quest;
    
}
