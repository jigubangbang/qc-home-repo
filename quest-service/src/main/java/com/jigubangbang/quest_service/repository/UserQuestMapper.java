package com.jigubangbang.quest_service.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.jigubangbang.quest_service.model.BadgeDto;
import com.jigubangbang.quest_service.model.QuestCerti;
import com.jigubangbang.quest_service.model.QuestDto;
import com.jigubangbang.quest_service.model.QuestImageDto;
import com.jigubangbang.quest_service.model.QuestModalDto;
import com.jigubangbang.quest_service.model.QuestSimpleParticipantDto;
import com.jigubangbang.quest_service.model.QuestUserDto;
import com.jigubangbang.quest_service.model.UserJourneyDto;

@Mapper
public interface UserQuestMapper {
    //퀘스트 모달 데이터 가져오기
    public QuestModalDto getQuestModalById(Map<String, Object> params);
    List<QuestSimpleParticipantDto> getInProgressUsers(int quest_id);
    List<QuestSimpleParticipantDto> getCompletedUsers(int quest_id);
    List<BadgeDto> getBadgesByQuestId(int quest_id);
    
    public int countUserQuest(Map<String, Object> params);
    public void insertQuestUser(QuestUserDto questUser);
    public void reChallengeQuestUser(Map<String, Object> params);
    
    public UserJourneyDto getUserJourney(String user_id);
    public List<QuestUserDto> getUserQuestList(Map<String, Object> params);
    public QuestCerti getQuestCerti(int quest_user_id);
    public List<String> getQuestCertiImages(int quest_user_id);

    public String getQuestType(int quest_user_id);
    public void updateQuestUserCompleted(Map<String, Object> params);
    public int updateQuestUserAbandon(int quest_user_id);
    public void insertQuestImages(List<QuestImageDto> images);


    List<QuestDto> getUserQuests(Map<String, Object> params);
    int countUserQuests(Map<String, Object> params);
}
