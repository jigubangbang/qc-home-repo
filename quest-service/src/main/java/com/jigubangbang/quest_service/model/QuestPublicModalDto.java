package com.jigubangbang.quest_service.model;

import java.sql.Timestamp;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuestPublicModalDto {
    private int id;             //quest id
    private String type;         // ENUM('AUTH', 'CHECK')
    private int category;
    private String title;
    private String description;
    private String difficulty;  // ENUM('EASY', 'MEDIUM', 'HARD')
    private int xp;
    private Boolean is_seasonal;
    private Timestamp season_start;
    private Timestamp season_end;
    private String status;       // ENUM( 'AUTH', 'CHECK' )

    //badge
    private List<BadgeDto> badges;

    //join
    private int count_in_progress;  //이 퀘스트를 진행 중인 사람
    private List<QuestSimpleParticipantDto> in_progress_user;
    private int count_completed;    //이 퀘스트를 진행 완료한 사람
    private List<QuestSimpleParticipantDto> completed_user;
}
