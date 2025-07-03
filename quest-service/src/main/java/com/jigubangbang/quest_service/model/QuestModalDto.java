package com.jigubangbang.quest_service.model;

import java.sql.Timestamp;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuestModalDto {
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

    //user
    private String user_id;
    private String nickname;

    //quest_user
    private String quest_status;    //quest_user가 있는지 있으면 quest_user의 status 없으면 not_started
    private int quest_user_id;
    private String started_at;
    private String completed_at;
    private String given_up_at;

    //badge
    private List<BadgeDto> badges;

    //join
    private int count_in_progress;  //이 퀘스트를 진행 중인 사람
    private List<QuestSimpleParticipantDto> in_progress_user;
    private int count_completed;    //이 퀘스트를 진행 완료한 사람
    private List<QuestSimpleParticipantDto> completed_user;
}
