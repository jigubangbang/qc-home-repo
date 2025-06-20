package com.jigubangbang.quest_service.model;

import java.sql.Timestamp;

import com.jigubangbang.quest_service.enums.Difficulty;
import com.jigubangbang.quest_service.enums.QuestStatus;
import com.jigubangbang.quest_service.enums.QuestType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuestListDto {
    private int id;
    private QuestType type;         // ENUM('AUTH', 'CHECK')
    private int category;
    private String title;
    private Difficulty difficulty;  // ENUM('EASY', 'MEDIUM', 'HARD')
    private int xp;
    private Boolean isSeasonal;
    private Timestamp season_start;
    private Timestamp season_end;
    private QuestStatus status;
    private Timestamp created_at;
}
