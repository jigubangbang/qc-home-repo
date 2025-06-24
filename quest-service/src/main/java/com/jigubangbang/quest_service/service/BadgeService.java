package com.jigubangbang.quest_service.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jigubangbang.quest_service.model.BadgeDto;
import com.jigubangbang.quest_service.model.BadgeQuestDto;
import com.jigubangbang.quest_service.model.UserBadgeDto;
import com.jigubangbang.quest_service.repository.BadgeMapper;

@Service
public class BadgeService {
    @Autowired
    private BadgeMapper badgeMapper;

    public Map<String, Object> getAllBadges(){
        List<BadgeDto> badges = badgeMapper.getAllBadges();
        int totalCount = badges.size();

        Map<String, Object> result = new HashMap<>();
        result.put("badges", badges);
        result.put("totalCount", totalCount);

        return result;
    }

    public Map<String, Object> searchBadges(String keyword){
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", "%"+keyword+"%");

        List<BadgeDto> badges = badgeMapper.searchBadges(params);
        int totalCount = badges.size();

        Map<String, Object> result = new HashMap<>();
        result.put("badges", badges);
        result.put("totalCount", totalCount);
        result.put("keyword", keyword);

        return result;
    }

    public BadgeDto getBadgeById(int badge_id){
        return badgeMapper.getBadgeById(badge_id);
    }

    public Map<String, Object> getUserBadgeInfo(String user_id){
        List<UserBadgeDto> badgeInfoList = badgeMapper.getUserBadgeInfo(user_id);
        
        for (UserBadgeDto badgeInfo : badgeInfoList){
            List<BadgeQuestDto> quests = badgeMapper.getBadgeQuests(badgeInfo.getBadge_id());
            badgeInfo.setQuests(quests);
            badgeInfo.setTotal_quest(quests.size());

            Map<String, Object> params = new HashMap<>();
            params.put("user_id", user_id);
            params.put("badge_id", badgeInfo.getBadge_id());
            int completedCount = badgeMapper.getUserCompletedQuestCount(params);
            badgeInfo.setCompleted_quest(completedCount);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("badges", badgeInfoList);
        result.put("totalCount", badgeInfoList.size());

        return result;
    }

    // public Map<String, Object> getUserBadges(String user_id){
    //     List<UserBadgeDto> userBadges = badgeMapper.getUserBadges(user_id);

    //     Map<String, Object> result = new HashMap<>();
    //     result.put("badges", userBadges);
    //     result.put("totalCount", userBadges.size());
    //     return result;
    // }

    public boolean pinBadge(String user_id, int badge_id){
        Map<String, Object> unpinParams = new HashMap<>();
        unpinParams.put("user_id", user_id);
        unpinParams.put("is_displayed", false);
        badgeMapper.updateBadgeDisplay(unpinParams);

        Map<String, Object> checkParams = new HashMap<>();
        checkParams.put("user_id", user_id);
        checkParams.put("badge_id", badge_id);
        UserBadgeDto userBadge = badgeMapper.getUserBadge(checkParams);

        if (userBadge != null){
            Map<String, Object> pinParams = new HashMap<>();
            pinParams.put("user_id", user_id);
            pinParams.put("badge_id", badge_id);
            pinParams.put("is_displayed", true);
            pinParams.put("pinned_at", new Timestamp(System.currentTimeMillis()));
            badgeMapper.updateBadgeDisplay(pinParams);
            return true;
        }
        return false;
    }

    public Map<String, Object> getAdminBadgeList(){
        Map<String, Object> params = new HashMap<>();
        
        List<BadgeDto> badges = badgeMapper.getAdminBadgeList(params);
        int totalCount = badges.size();
        
        Map<String, Object> result = new HashMap<>();
        result.put("badges", badges);
        result.put("totalCount", totalCount);
        
        return result;
    }

    public BadgeDto createBadge(BadgeDto badge){
        badge.setCreated_at(new Timestamp(System.currentTimeMillis()));
        badgeMapper.createBadge(badge);
        return badge;
    }

    public void deleteBadge(int badge_id){
        badgeMapper.deleteBadge(badge_id);
    }

    
    
}
