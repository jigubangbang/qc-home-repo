package com.jigubangbang.quest_service.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.jigubangbang.quest_service.model.QuestCerti;
import com.jigubangbang.quest_service.model.QuestImageDto;
import com.jigubangbang.quest_service.model.QuestUserDto;
import com.jigubangbang.quest_service.model.UserJourneyDto;

@Mapper
public interface UserQuestMapper {
    public int countUserQuest(Map<String, Object> params);
    public void insertQuestUser(QuestUserDto questUser);
    public UserJourneyDto getUserJourney(String user_id);
    public List<QuestUserDto> getUserQuestList(Map<String, Object> params);
    public QuestCerti getQuestCerti(int quest_user_id);
    public List<String> getQuestCertiImages(int quest_user_id);

    public String getQuestType(int quest_user_id);
    public void updateQuestUserPending(int quest_user_id);
    public int updateQuestUserAbandon(int quest_user_id);
    public void insertQuestImages(List<QuestImageDto> images);


}
