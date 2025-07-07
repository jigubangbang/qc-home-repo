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
}
