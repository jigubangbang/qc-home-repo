package com.jigubangbang.quest_service.model;

import java.sql.Timestamp;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadgePublicModalDto {
    //badge
    private int id;                     //badge_id
    private String kor_title;
    private String eng_title;
    private String description;
    private String icon;
    private String difficulty;
    private Timestamp created_at;

    //badge_quest, quest
    //badge_quest에서 badge_id가 같은 행의 모든 quest의 리스트
    //Integer: quest_id, String: quest 테이블에서 title
    public List<QuestDto> quest_list;   

    //quest_user
    private int completed_quest;    //quest_list의 퀘스트 중 user_id의 유저가 완료한 퀘스트
    private int total_quest;        //quest_list의 퀘스트 전체 개수

    //join
    private int count_awarded;    //이 뱃지를 얻은 사람의 수
    private List<QuestSimpleParticipantDto> awarded_user;   //Dto의 이름은 다르지만 뱃지를 얻은 사람
}



    