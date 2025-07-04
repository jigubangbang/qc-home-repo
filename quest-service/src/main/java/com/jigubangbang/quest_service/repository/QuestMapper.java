package com.jigubangbang.quest_service.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.jigubangbang.quest_service.model.BadgeDto;
import com.jigubangbang.quest_service.model.QuestCategoryDto;
import com.jigubangbang.quest_service.model.QuestDetailDto;
import com.jigubangbang.quest_service.model.QuestDto;
import com.jigubangbang.quest_service.model.QuestParticipantDto;
import com.jigubangbang.quest_service.model.QuestPublicModalDto;
import com.jigubangbang.quest_service.model.QuestSimpleParticipantDto;

@Mapper
public interface QuestMapper {
    public List<QuestDto> getQuests(Map<String, Object> params);
    public int countQuests(Map<String, Object> params);

    public List<QuestParticipantDto> getQuestParticipants(int quest_id);
    public int countQuestParticipants(int quest_id);

    public QuestDto selectQuestById(int quest_id);
    
    //public modal
    public QuestPublicModalDto getQuestModalById(int quest_id);
    List<QuestSimpleParticipantDto> getInProgressUsers(int quest_id);
    List<QuestSimpleParticipantDto> getCompletedUsers(int quest_id);
    List<BadgeDto> getBadgesByQuestId(int quest_id);

    //myPage
    Map<String, Object> getUserInfo(String userId);
    List<QuestDetailDto> getUserInProgressQuests(String userId);
    List<QuestDetailDto> getUserCompletedQuests(String userId);
    List<QuestCategoryDto> getQuestCategoriesWithUserStats(String userId);
    int getTotalQuestCount();
    BadgeDto getUserPinnedBadge(String userId);
    Map<String, Integer> getUserBadgeCounts(String userId);
}
