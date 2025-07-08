package com.jigubangbang.quest_service.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.jigubangbang.quest_service.model.BadgeDto;
import com.jigubangbang.quest_service.model.BadgeModalDto;
import com.jigubangbang.quest_service.model.BadgePublicModalDto;
import com.jigubangbang.quest_service.model.BadgeQuestDto;
import com.jigubangbang.quest_service.model.QuestDto;
import com.jigubangbang.quest_service.model.QuestSimpleParticipantDto;
import com.jigubangbang.quest_service.model.UserBadgeDto;

@Mapper
public interface BadgeMapper {
    public List<BadgeDto> getAllBadges(Map<String, Object> params);
    public int getBadgeCount(Map<String, Object> params);

    //모달
    public BadgeModalDto getBadgeModalBase(Map<String, Object> params);
    public BadgePublicModalDto getBadgePublicModalBase(int badge_id);
    public List<QuestDto> getQuestListByBadgeId(int badge_id);
    public int getCompletedQuestCount(Map<String, Object> params);
    public int getAwardedUserCount(int badge_id);
    public List<QuestSimpleParticipantDto> getAwardedUserList(int badge_id);

    public List<String> getQuestTitlesByBadgeId(int id);
    public BadgeDto getBadgeById(int badge_id);
    
    public List<UserBadgeDto> getUserBadgeInfo(String user_id);
    //UserBadgeDto getUserBadgeInfoById(Map<String, Object> params);
    
    public List<UserBadgeDto> getUserBadges(String user_id);
    public UserBadgeDto getUserBadge(Map<String, Object> params);
    public int updateBadgeDisplay(Map<String, Object> params);
    public int insertUserBadge(UserBadgeDto userBadge);

    public List<BadgeQuestDto> getBadgeQuests(int badge_id);
    public int getUserCompletedQuestCount(Map<String, Object> params);

    public List<BadgeDto> getAdminBadgeList(Map<String, Object> params);
    public int createBadge(BadgeDto badge);
    //int updateBadge(BadgeDto badge);
    public int deleteBadge(int badge_id);
}
