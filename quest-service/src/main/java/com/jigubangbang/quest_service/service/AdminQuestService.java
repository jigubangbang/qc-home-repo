package com.jigubangbang.quest_service.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jigubangbang.quest_service.model.AdminQuestDetailDto;
import com.jigubangbang.quest_service.model.AdminQuestDto;
import com.jigubangbang.quest_service.model.AdminQuestUserDto;
import com.jigubangbang.quest_service.model.BadgeQuestDto;
import com.jigubangbang.quest_service.model.QuestCerti;
import com.jigubangbang.quest_service.model.QuestDto;
import com.jigubangbang.quest_service.model.SimpleBadgeDto;
import com.jigubangbang.quest_service.repository.AdminQuestMapper;

@Service
public class AdminQuestService {
    @Autowired
    private AdminQuestMapper adminQuestMapper;


    public Map<String, Object> getQuestList(int pageNum, String search, String sortOption, int limit, String status) {
        Map<String, Object> result = new HashMap<>();
        
        // 페이지네이션 계산
        int offset = (pageNum - 1) * limit;
        
        // 파라미터 맵 생성
        Map<String, Object> params = new HashMap<>();
        params.put("search", search.trim());
        params.put("sortOption", sortOption);
        params.put("limit", limit);
        params.put("offset", offset);
        params.put("status", status);
        
        //status 변경
        adminQuestMapper.updateQuestStatus();
        List<AdminQuestDto> questList = adminQuestMapper.selectQuestList(params);
        
        int totalCount = adminQuestMapper.selectQuestCount(params);
        
        int totalPages = (int) Math.ceil((double) totalCount / limit);
        
        result.put("questList", questList);
        result.put("totalCount", totalCount);
        result.put("currentPage", pageNum);
        result.put("totalPages", totalPages);
        
        return result;
    }

    public AdminQuestDetailDto getQuestDetail(int quest_id) {
        // 퀘스트 기본 정보 조회
        AdminQuestDetailDto questDetail = adminQuestMapper.selectQuestDetail(quest_id);
        
        if (questDetail == null) {
            return null;
        }
        
        // 퀘스트 통계 정보 조회
        int completedCount = adminQuestMapper.selectQuestCompletedCount(quest_id);
        int inProgressCount = adminQuestMapper.selectQuestInProgressCount(quest_id);
        int givenUpCount = adminQuestMapper.selectQuestGivenUpCount(quest_id);
        
        questDetail.setCount_completed(completedCount);
        questDetail.setCount_in_progress(inProgressCount);
        questDetail.setCount_given_up(givenUpCount);
        
        return questDetail;
    }

    public Map<String, Object> getQuestBadges(int quest_id){
        Map<String, Object> result = new HashMap<>();
        List<SimpleBadgeDto> badges = adminQuestMapper.selectQuestBadges(quest_id);
        int totalCount = adminQuestMapper.selectQuestBadgesCount(quest_id);
        
        result.put("questBadges", badges);
        result.put("totalCount", totalCount); 
        return result;
    }
    
    public Map<String, Object> getQuestUsers(int quest_id, int pageNum, int limit) {
        Map<String, Object> result = new HashMap<>();
        
        int offset = (pageNum - 1) * limit;
        
        Map<String, Object> params = new HashMap<>();
        params.put("quest_id", quest_id);
        params.put("limit", limit);
        params.put("offset", offset);
        
        // 퀘스트 참여자 목록 조회
        List<AdminQuestUserDto> questUsers = adminQuestMapper.selectQuestUsers(params);
        
        // 각 참여자의 이미지 목록 조회
        for (AdminQuestUserDto user : questUsers) {
            List<String> images = adminQuestMapper.selectQuestUserImages(user.getQuest_user_id());
            user.setImages(images);
        }
        
        // 전체 참여자 수 조회
        int totalCount = adminQuestMapper.selectQuestUsersCount(quest_id);
        int totalPages = (int) Math.ceil((double) totalCount / limit);
        
        result.put("questUsers", questUsers);
        result.put("totalCount", totalCount);
        result.put("currentPage", pageNum);
        result.put("totalPages", totalPages);
        return result;
    }

    public QuestDto createQuest(QuestDto quest){
        adminQuestMapper.createQuest(quest);
        return quest;
    }

    public QuestDto updateQuest(int quest_id, QuestDto quest){
        Map<String, Object> params = new HashMap<>();
        params.put("quest_id", quest_id);
        params.put("type", quest.getType());
        params.put("category", quest.getCategory());
        params.put("title", quest.getTitle());
        params.put("difficulty", quest.getDifficulty());
        params.put("xp", quest.getXp());
        params.put("isSeasonal", quest.getIs_seasonal());
        params.put("seasonStart", quest.getSeason_start());
        params.put("seasonEnd", quest.getSeason_end());
        params.put("status", quest.getStatus());
        
        adminQuestMapper.updateQuest(params);
        return quest;
    }

    public void deleteQuest(int quest_id){
        adminQuestMapper.deleteQuest(quest_id);
    }

    public Map<String, Object> getQuestCertiList(int pageNum, String sortOption, String status){
        //#NeedToChange
        int questCertiPerPage = 100;
        int offset=(pageNum-1)*questCertiPerPage;

        Map<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("questCertiPerPage", questCertiPerPage);
        if (sortOption != null){
            params.put("sortOption", sortOption);
        }
        if (status != null){
            params.put("status", status);
        }

        List<QuestCerti> questCertis = adminQuestMapper.getQuestCertiList(params);
        int totalCount = adminQuestMapper.countQuestCerti(params);
        int pageCount = (int) Math.ceil((double) totalCount/questCertiPerPage);

        Map<String, Object> result = new HashMap<>();
        result.put("questCertis", questCertis);
        result.put("pageCount", pageCount);
        result.put("totalCount", totalCount);

        return result;
    }

    public Map<String, Object> getQuestCerti(int quest_user_id){
        Map<String, Object> result = new HashMap<>();
        
        QuestCerti questCerti = adminQuestMapper.getQuestCerti(quest_user_id);
        if (questCerti != null){
            List<String> imageList = adminQuestMapper.getQuestCertiImageList(quest_user_id);
            questCerti.setImage_list(imageList);

            result.put("success", true);
            result.put("data", questCerti);
        }else{
            result.put("success", false);
            result.put("message", "퀘스트를 찾을 수 없습니다");
        }
        return result;
    }


    @Transactional
    public void rejectQuest(int quest_user_id, int quest_id, int xp, String user_id) {
        String currentStatus = adminQuestMapper.getQuestUserStatus(quest_user_id);
        if (!"COMPLETED".equals(currentStatus)) {
            throw new IllegalStateException("완료된 퀘스트만 취소할 수 있습니다");
        }
        
        try {
            adminQuestMapper.updateQuestUserReject(quest_user_id);
            adminQuestMapper.deleteQuestImage(quest_user_id);

            List<Integer> relatedBadgeIds = adminQuestMapper.getBadgeIdsByQuestId(quest_id);

            for (int badge_id : relatedBadgeIds){
                boolean alreadyHasBadge = false;

                if (adminQuestMapper.checkUserHasBadge(user_id, badge_id)>0){
                    alreadyHasBadge = true;
                }

                if(alreadyHasBadge){
                    Map<String, Object> params2 = new HashMap<>();
                    params2.put("badge_id", badge_id);
                    params2.put("user_id", user_id);
                    int isBadgeRemoved = adminQuestMapper.deleteBadgeUser(params2);

                    if(isBadgeRemoved > 0){
                        System.out.println("뱃지 없어짐1!!!");
                        //#NeedToChange 여기서 뱃지 뺏긴 알림이 오는 거임
                    }
                }
            }
 
            Map<String, Object> params = new HashMap<>();
            params.put("xp", -xp);
            params.put("user_id", user_id);
            
            adminQuestMapper.updateUserXp(params);
            adminQuestMapper.updateUserLevel(user_id);
        } catch (Exception e) {
            throw new RuntimeException("퀘스트 인증 취소 중 오류 발생", e);
        }
    }
}
