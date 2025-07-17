package com.jigubangbang.quest_service.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jigubangbang.quest_service.model.AdminBadgeDto;
import com.jigubangbang.quest_service.model.AdminQuestDetailDto;
import com.jigubangbang.quest_service.model.BadgeCreateRequest;
import com.jigubangbang.quest_service.model.BadgeDto;
import com.jigubangbang.quest_service.model.BadgeIdCheckResponse;
import com.jigubangbang.quest_service.model.BadgeModalDto;
import com.jigubangbang.quest_service.model.BadgePublicModalDto;
import com.jigubangbang.quest_service.model.BadgeQuestDto;
import com.jigubangbang.quest_service.model.BadgeUpdateRequest;
import com.jigubangbang.quest_service.model.QuestDto;
import com.jigubangbang.quest_service.model.QuestSimpleParticipantDto;
import com.jigubangbang.quest_service.model.UserBadgeDto;
import com.jigubangbang.quest_service.repository.BadgeMapper;

@Service
public class BadgeService {
    @Autowired
    private BadgeMapper badgeMapper;

    public Map<String, Object> getAllBadges(int pageNum, String search, int limit){
        Map<String, Object> params = new HashMap<>();

        if (search != null && !search.isEmpty()) {
            params.put("search", search);  // 추가
        }
        
        int offset = (pageNum-1)*limit;
        params.put("limit", limit);
        params.put("offset", offset);

        List<BadgeDto> badges = badgeMapper.getAllBadges(params);
        int totalCount = badgeMapper.getBadgeCount(params);

        for(BadgeDto badge : badges) {
        List<String> questTitles = badgeMapper.getQuestTitlesByBadgeId(badge.getId());
        badge.setQuest(questTitles != null ? questTitles : new ArrayList<>());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("badges", badges);
        result.put("totalCount", totalCount);

        return result;
    }


    public BadgeDto getBadgeById(int badge_id){
        return badgeMapper.getBadgeById(badge_id);
    }

    public BadgeModalDto getBadgeModal(int badge_id, String user_id){
        //기본 정보
        Map<String, Object> params = new HashMap<>();
        params.put("badge_id", badge_id);
        params.put("user_id", user_id);
        BadgeModalDto badgeModal = badgeMapper.getBadgeModalBase(params);

        if(badgeModal == null){
            return null;
        }

        //퀘스트 리스트
        List<QuestDto> questList = badgeMapper.getQuestListByBadgeId(badge_id);
        badgeModal.setQuest_list(questList);

        //완료된 퀘스트 수 조회
        int completedQuest = badgeMapper.getCompletedQuestCount(params);
        badgeModal.setCompleted_quest(completedQuest);
        badgeModal.setTotal_quest(questList.size());

        //뱃지 획득자 조회
        int countAwarded = badgeMapper.getAwardedUserCount(badge_id);
        badgeModal.setCount_awarded(countAwarded);

        //뱃지 획득자 리스트
        List<QuestSimpleParticipantDto> awardedUsers = badgeMapper.getAwardedUserList(badge_id);
        badgeModal.setAwarded_user(awardedUsers);
        return badgeModal;
    } 

    public BadgePublicModalDto getBadgePublicModal(int badge_id){
        //기본 정보
        BadgePublicModalDto badgeModal = badgeMapper.getBadgePublicModalBase(badge_id);

        if(badgeModal == null){
            return null;
        }
        
        //퀘스트 리스트
        List<QuestDto> questList = badgeMapper.getQuestListByBadgeId(badge_id);
        badgeModal.setQuest_list(questList);

        //뱃지 획득자 조회
        int countAwarded = badgeMapper.getAwardedUserCount(badge_id);
        badgeModal.setCount_awarded(countAwarded);

        //뱃지 획득자 리스트
        List<QuestSimpleParticipantDto> awardedUsers = badgeMapper.getAwardedUserList(badge_id);
        badgeModal.setAwarded_user(awardedUsers);
        return badgeModal;
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

    public Map<String, Object> getUserBadges(String user_id){
        List<UserBadgeDto> userBadges = badgeMapper.getUserBadges(user_id);

        Map<String, Object> result = new HashMap<>();
        result.put("badges", userBadges);
        result.put("totalCount", userBadges.size());
        return result;
    }

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
        List<AdminBadgeDto> badges = badgeMapper.getAdminBadgeList();
    
        for (AdminBadgeDto badge : badges) {
            List<String> questTitles = badgeMapper.getQuestTitlesByBadgeId(badge.getBadge_id());
            badge.setQuest(questTitles);
        }
        
        int totalCount = badges.size();
        
        Map<String, Object> result = new HashMap<>();
        result.put("badges", badges);
        result.put("totalCount", totalCount);
        
        return result;
    }

    @Transactional
    public int createBadge(BadgeCreateRequest request) {
        try {
            // 1. 뱃지 기본 정보 삽입
            badgeMapper.insertBadge(request);
            
            // 2. 퀘스트 연결 정보 삽입 (quest_ids가 있는 경우에만)
            if (request.getQuest_ids() != null && !request.getQuest_ids().isEmpty()) {
                for (Integer questId : request.getQuest_ids()) {
                    badgeMapper.insertBadgeQuest(request.getId(), questId);
                }
            }
            
            return request.getId();
            
        } catch (Exception e) {
            throw new RuntimeException("뱃지 생성 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    public boolean existsBadgeById(int badgeId) {
        return badgeMapper.existsBadgeById(badgeId);
    }




    public BadgeIdCheckResponse checkBadgeIdAvailability(int badgeId) {
        BadgeIdCheckResponse response = new BadgeIdCheckResponse();
        
        try {
            // 해당 ID가 이미 존재하는지 확인
            boolean exists = badgeMapper.existsBadgeById(badgeId);
            
            if (!exists) {
                // 사용 가능한 경우
                response.setAvailable(true);
                response.setSuggestedId(null);
                response.setMessage("사용 가능한 ID입니다.");
            } else {
                // 사용 불가능한 경우, 추천 ID 찾기
                Integer suggestedId = findNextAvailableId();
                response.setAvailable(false);
                response.setSuggestedId(suggestedId);
                response.setMessage("이미 사용 중인 ID입니다.");
            }
            
        } catch (Exception e) {
            // 예외 발생 시 안전하게 사용 불가능으로 처리
            response.setAvailable(false);
            response.setSuggestedId(null);
            response.setMessage("ID 확인 중 오류가 발생했습니다.");
        }
        
        return response;
    }

    private Integer findNextAvailableId() {
        try {
            // 1부터 시작해서 사용 가능한 가장 작은 ID 찾기
            Integer nextId = badgeMapper.findNextAvailableId();
            return nextId != null ? nextId : 1; // null인 경우 1 반환
        } catch (Exception e) {
            // 에러 발생 시 null 반환
            return null;
        }
    }

    @Transactional
    public void updateBadge(int badge_id, BadgeUpdateRequest request) {
        // 1. 입력값 검증
        if (request.getKor_title() == null || request.getKor_title().trim().isEmpty()) {
            throw new IllegalArgumentException("한국어 제목은 필수입니다.");
        }
        if (request.getEng_title() == null || request.getEng_title().trim().isEmpty()) {
            throw new IllegalArgumentException("영어 제목은 필수입니다.");
        }
        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("설명은 필수입니다.");
        }
        
        // 2. 뱃지 존재 여부 확인
        if (!badgeMapper.badgeExists(badge_id)) {
            throw new IllegalArgumentException("존재하지 않는 뱃지입니다. Badge ID: " + badge_id);
        }
        
        // 3. 뱃지 기본 정보 업데이트
        int updatedRows = badgeMapper.updateBadgeInfo(badge_id, request);
        if (updatedRows == 0) {
            throw new RuntimeException("뱃지 정보 업데이트에 실패했습니다.");
        }
        
        // 4. 퀘스트 연결 업데이트 (퀘스트가 있는 경우에만)
        if (request.hasQuestUpdates()) {
            // 4-1. 퀘스트 존재 여부 확인
            for (Integer quest_id : request.getQuest_ids()) {
                if (!badgeMapper.questExists(quest_id)) {
                    throw new IllegalArgumentException("존재하지 않는 퀘스트입니다. Quest ID: " + quest_id);
                }
            }
            
            // 4-2. 기존 연결 삭제
            badgeMapper.deleteBadgeQuestConnections(badge_id);
            
            // 4-3. 새로운 연결 추가
            for (Integer quest_id : request.getQuest_ids()) {
                badgeMapper.insertBadgeQuestConnection(badge_id, quest_id);
            }
        } else {
            // 퀘스트 목록이 비어있으면 모든 연결 삭제
            badgeMapper.deleteBadgeQuestConnections(badge_id);
        }
    }

    @Transactional
    public void deleteBadge(int badge_id){
            try {
            // 1. 배지 관련 퀘스트 삭제 
            badgeMapper.deleteBadgeQuest(badge_id);
            
            // 2. 배지 관련 사용자 삭제 
            badgeMapper.deleteBadgeUser(badge_id);
            
            // 3. 배지 삭제
            badgeMapper.deleteBadge(badge_id);
            
        } catch (Exception e) {
            // 트랜잭션 롤백을 위해 RuntimeException 발생
            throw new RuntimeException("배지 삭제 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    public Map<String, Object> getBadgeDetail(int badge_id) {
        Map<String, Object> result = new HashMap<>();
        
        // 뱃지 기본 정보 조회
        BadgeDto badge = badgeMapper.getBadgeById(badge_id);
        if (badge == null) {
            throw new RuntimeException("뱃지를 찾을 수 없습니다. Badge ID: " + badge_id);
        }
        
        // 뱃지와 연관된 퀘스트 목록 조회
        List<AdminQuestDetailDto> questList = badgeMapper.getQuestsByBadgeId(badge_id);
        
        // 뱃지 획득 사용자 수 조회
        int awardedCount = badgeMapper.getAwardedUserCount(badge_id);
        
        // 뱃지 획득 사용자 목록 조회 (최대 10명)
        List<Map<String, Object>> awardedUsers = badgeMapper.getAwardedUsers(badge_id);
        
        result.put("id", badge.getId());
        result.put("kor_title", badge.getKor_title());
        result.put("eng_title", badge.getEng_title());
        result.put("description", badge.getDescription());
        result.put("icon", badge.getIcon());
        result.put("difficulty", badge.getDifficulty());
        result.put("created_at", badge.getCreated_at());
        result.put("quest_list", questList);
        result.put("count_awarded", awardedCount);
        result.put("awarded_user", awardedUsers);
        
        return result;
    }
    
}
