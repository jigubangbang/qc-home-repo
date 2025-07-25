package com.jigubangbang.quest_service.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jigubangbang.quest_service.model.BadgeDto;
import com.jigubangbang.quest_service.model.QuestCategoryDto;
import com.jigubangbang.quest_service.model.QuestCerti;
import com.jigubangbang.quest_service.model.QuestDetailDto;
import com.jigubangbang.quest_service.model.QuestDto;
import com.jigubangbang.quest_service.model.QuestParticipantDto;
import com.jigubangbang.quest_service.model.QuestPublicModalDto;
import com.jigubangbang.quest_service.model.QuestSimpleParticipantDto;
import com.jigubangbang.quest_service.model.SimpleUserDto;
import com.jigubangbang.quest_service.model.UserBadgeDto;
import com.jigubangbang.quest_service.model.UserLevelInfoDto;
import com.jigubangbang.quest_service.repository.BadgeMapper;
import com.jigubangbang.quest_service.repository.QuestMapper;

@Service
public class QuestService {
    @Autowired
    private QuestMapper questMapper;

    @Autowired
    private BadgeMapper badgeMapper;

    public Map<String, Object> getQuests(int pageNum, int category, String sortOption, String difficulty, String search, int limit){
        Map<String, Object> params = new HashMap<>();
        if (category != 0){
            params.put("category", category);
        }
        if (sortOption != null && !sortOption.isEmpty()) {
            params.put("sortOption", sortOption);
        }
        if (difficulty != null && !difficulty.isEmpty()) {
            params.put("difficulty", difficulty);
        }
        if (search != null && !search.isEmpty()) {
            params.put("search", search);  // 추가
        }
        
        int offset = (pageNum-1)*limit;
        params.put("limit", limit);
        params.put("offset", offset);

        List<QuestDto> quests = questMapper.getQuests(params);
        int totalCount = questMapper.countQuests(params);
        int pageCount = (int) Math.ceil((double) totalCount/limit);

        Map<String, Object> result = new HashMap<>();
        result.put("quests", quests);
        result.put("pageCount", pageCount);
        result.put("totalCount", totalCount);
        result.put("currentPage", pageNum);
        return result;
    }

    public Map<String, Object> getQuestParticipants(int quest_id){
        List<QuestParticipantDto> participants = questMapper.getQuestParticipants(quest_id);
        int totalCount = questMapper.countQuestParticipants(quest_id);

        Map<String, Object> result = new HashMap<>();
        result.put("participants", participants);
        result.put("totalCount", totalCount);
        
        return result;
    }

    public QuestDto getQuestById(int quest_id){
        return questMapper.selectQuestById(quest_id);
    }

    public QuestPublicModalDto getQuestPublicModalById(int quest_id){
        QuestPublicModalDto questModal = questMapper.getQuestModalById(quest_id);

        if (questModal == null){
            return null;
        }

        List<BadgeDto> badges = questMapper.getBadgesByQuestId(quest_id);
        questModal.setBadges(badges);

        List<QuestSimpleParticipantDto> inProgressUsers = questMapper.getInProgressUsers(quest_id);
        questModal.setIn_progress_user(inProgressUsers);
        
        List<QuestSimpleParticipantDto> completedUsers = questMapper.getCompletedUsers(quest_id);
        questModal.setCompleted_user(completedUsers);

        return questModal;
    }

    public Map<String, Object> getUserPageData(String userId) {
        Map<String, Object> result = new HashMap<>();
        
        // 유저 기본 정보
        Map<String, Object> userInfo = questMapper.getUserInfo(userId);
        result.put("user", userInfo);
        
        // 퀘스트 정보
        Map<String, Object> questInfo = new HashMap<>();
        
        // 진행 중인 퀘스트
        List<QuestDetailDto> inProgressQuests = questMapper.getUserInProgressQuests(userId);
        questInfo.put("count_in_progress", inProgressQuests.size());
        questInfo.put("in_progress_quests", inProgressQuests);
        
        // 완료된 퀘스트
        List<QuestDetailDto> completedQuests = questMapper.getUserCompletedQuests(userId);
        questInfo.put("count_completed", completedQuests.size());
        questInfo.put("completed_quests", completedQuests);
        
        // 카테고리별 퀘스트 정보 (각 카테고리의 진행중/완료/전체 퀘스트 수)
        List<QuestCategoryDto> totalQuests = questMapper.getQuestCategoriesWithUserStats(userId);
        questInfo.put("total_quests", totalQuests);
        
        // 전체 퀘스트 수 (시스템 전체의 모든 퀘스트 개수, 유저와 관계없음)
        int totalQuestCount = questMapper.getTotalQuestCount();
        questInfo.put("count_total_quest", totalQuestCount);
        
        result.put("quest", questInfo);
        
        // 뱃지 정보
        Map<String, Object> badgeInfo = new HashMap<>();
        
        // 대표 뱃지
        BadgeDto pinnedBadge = questMapper.getUserPinnedBadge(userId);
        badgeInfo.put("pinned_badge", pinnedBadge);
        
        // 모든 뱃지 정보 (획득 여부 포함) - badgeMapper의 기존 메서드 활용
        List<UserBadgeDto> badges = badgeMapper.getUserBadgeInfo(userId);
        badgeInfo.put("badges", badges);
        
        // 뱃지 카운트
        Map<String, Integer> badgeCounts = questMapper.getUserBadgeCounts(userId);
        badgeInfo.put("count_total_badge", badgeCounts.get("total"));
        badgeInfo.put("count_awarded_badge", badgeCounts.get("awarded"));
        
        result.put("badge", badgeInfo);
        
        return result;
    }

    public QuestCerti getQuestCerti(int quest_user_id){
        QuestCerti questCerti = questMapper.getQuestCerti(quest_user_id);
        if (questCerti != null){
            List<String> images = questMapper.getQuestCertiImages(quest_user_id);
            questCerti.setImage_list(images);
        }
        return questCerti;
    }

    public List<SimpleUserDto> getUserGraph(){
        return questMapper.getUserGraph();
    }

    public int getUserRank(String user_id){
        return questMapper.getUserRank(user_id);
    }

    public int getCountUser(){
        return questMapper.getCountUser();
    }

    public UserLevelInfoDto getUserLevelInfo(String userId) {
        // 사용자 기본 정보 조회 (xp, level)
        UserLevelInfoDto userInfo = questMapper.getUserBasicInfo(userId);
        
        if (userInfo == null) {
            return null;
        }
        
        // 레벨 정보 계산
        calculateLevelInfo(userInfo);
        
        return userInfo;
    }
    
    private void calculateLevelInfo(UserLevelInfoDto userInfo) {
        int currentLevel = userInfo.getCurrentLevel();
        int currentXp = userInfo.getCurrentXp();

        int currentLevelRequiredXp = getXpRequiredForLevel(currentLevel);
        userInfo.setXpRequiredForCurrentLevel(currentLevelRequiredXp);
        
        // 최대 레벨 체크
        if (currentLevel >= 100) {
            userInfo.setMaxLevel(true);
            userInfo.setNextLevel(100);
            userInfo.setXpRequiredForNextLevel(29900);
            userInfo.setXpNeededForNextLevel(0);
            return;
        }
        
        userInfo.setMaxLevel(false);
        userInfo.setNextLevel(currentLevel + 1);
        
        // 다음 레벨에 필요한 총 XP 계산
        int nextLevelRequiredXp = getXpRequiredForLevel(currentLevel + 1);
        userInfo.setXpRequiredForNextLevel(nextLevelRequiredXp);
        
        // 다음 레벨까지 부족한 XP 계산
        int xpNeeded = Math.max(0, nextLevelRequiredXp - currentXp);
        userInfo.setXpNeededForNextLevel(xpNeeded);
    }
    
    private int getXpRequiredForLevel(int level) {
        switch (level) {
            case 1: return 0;
            case 2: return 0;
            case 3: return 100;
            case 4: return 200;
            case 5: return 300;
            case 6: return 400;
            case 7: return 500;
            case 8: return 600;
            case 9: return 700;
            case 10: return 800;
            case 11: return 900;
            case 12: return 1000;
            case 13: return 1100;
            case 14: return 1200;
            case 15: return 1300;
            case 16: return 1400;
            case 17: return 1500;
            case 18: return 1600;
            case 19: return 1700;
            case 20: return 1800;
            case 21: return 1900;
            case 22: return 2100;
            case 23: return 2300;
            case 24: return 2500;
            case 25: return 2700;
            case 26: return 2900;
            case 27: return 3100;
            case 28: return 3300;
            case 29: return 3500;
            case 30: return 3700;
            case 31: return 3900;
            case 32: return 4100;
            case 33: return 4300;
            case 34: return 4500;
            case 35: return 4700;
            case 36: return 4900;
            case 37: return 5100;
            case 38: return 5300;
            case 39: return 5500;
            case 40: return 5700;
            case 41: return 5900;
            case 42: return 6200;
            case 43: return 6500;
            case 44: return 6800;
            case 45: return 7100;
            case 46: return 7400;
            case 47: return 7700;
            case 48: return 8000;
            case 49: return 8300;
            case 50: return 8600;
            case 51: return 8900;
            case 52: return 9200;
            case 53: return 9500;
            case 54: return 9800;
            case 55: return 10100;
            case 56: return 10400;
            case 57: return 10700;
            case 58: return 11000;
            case 59: return 11300;
            case 60: return 11600;
            case 61: return 11900;
            case 62: return 12300;
            case 63: return 12700;
            case 64: return 13100;
            case 65: return 13500;
            case 66: return 13900;
            case 67: return 14300;
            case 68: return 14700;
            case 69: return 15100;
            case 70: return 15500;
            case 71: return 15900;
            case 72: return 16300;
            case 73: return 16700;
            case 74: return 17100;
            case 75: return 17500;
            case 76: return 17900;
            case 77: return 18300;
            case 78: return 18700;
            case 79: return 19100;
            case 80: return 19500;
            case 81: return 19900;
            case 82: return 20400;
            case 83: return 20900;
            case 84: return 21400;
            case 85: return 21900;
            case 86: return 22400;
            case 87: return 22900;
            case 88: return 23400;
            case 89: return 23900;
            case 90: return 24400;
            case 91: return 24900;
            case 92: return 25400;
            case 93: return 25900;
            case 94: return 26400;
            case 95: return 26900;
            case 96: return 27400;
            case 97: return 27900;
            case 98: return 28400;
            case 99: return 28900;
            case 100: return 29900;
            default: return 29900; 
        }
    }
}
