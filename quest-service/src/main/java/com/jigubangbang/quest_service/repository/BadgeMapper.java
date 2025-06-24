package com.jigubangbang.quest_service.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.jigubangbang.quest_service.model.BadgeDto;
import com.jigubangbang.quest_service.model.BadgeQuestDto;
import com.jigubangbang.quest_service.model.QuestCategoryDto;
import com.jigubangbang.quest_service.model.UserBadgeDto;

@Mapper
public interface BadgeMapper {
    List<BadgeDto> getAllBadges();
    List<BadgeDto> searchBadges(Map<String, Object> params);
    BadgeDto getBadgeById(int badge_id);
    
    List<UserBadgeDto> getUserBadgeInfo(String user_id);
    //UserBadgeDto getUserBadgeInfoById(Map<String, Object> params);
    
    List<UserBadgeDto> getUserBadges(String user_id);
    UserBadgeDto getUserBadge(Map<String, Object> params);
    int updateBadgeDisplay(Map<String, Object> params);
    int insertUserBadge(UserBadgeDto userBadge);

    List<BadgeQuestDto> getBadgeQuests(int badge_id);
    int getUserCompletedQuestCount(Map<String, Object> params);

    List<BadgeDto> getAdminBadgeList(Map<String, Object> params);
    int createBadge(BadgeDto badge);
    int updateBadge(BadgeDto badge);
    int deleteBadge(int badge_id);

}
