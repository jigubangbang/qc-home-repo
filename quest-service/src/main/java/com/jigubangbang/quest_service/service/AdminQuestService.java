package com.jigubangbang.quest_service.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jigubangbang.quest_service.model.AdminQuestDetailDto;
import com.jigubangbang.quest_service.model.AdminQuestDto;
import com.jigubangbang.quest_service.model.AdminQuestUserDto;
import com.jigubangbang.quest_service.model.QuestCerti;
import com.jigubangbang.quest_service.model.QuestDto;
import com.jigubangbang.quest_service.repository.AdminQuestMapper;
import com.jigubangbang.quest_service.repository.QuestMapper;

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
        result.put("pageSize", limit);
        result.put("hasNext", pageNum < totalPages);
        result.put("hasPrevious", pageNum > 1);
        
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


    //userQuest로 이동
    // public Map<String, Object> approveQuest(int quest_user_id){
    //     Map<String, Object> result = new HashMap<>();
    //     result.put("badgeAwarded", false);
    //     result.put("awardedBadges", new ArrayList<>());

    //     adminQuestMapper.updateQuestUserApprove(quest_user_id);

    //     QuestCerti questCerti = adminQuestMapper.getQuestCerti(quest_user_id);
    //     QuestDto quest = questMapper.selectQuestById(questCerti.getQuest_id());
        
    //     if (quest != null && quest.getXp() > 0 ){
    //         Map<String, Object> params = new HashMap<>();
    //         params.put("user_id", questCerti.getUser_id());
    //         params.put("xp", quest.getXp());
    //         adminQuestMapper.updateUserXp(params);
    //     }

    //     if (questCerti != null){
    //         List<Integer> awardedBadges = checkAndAwardBadges(questCerti.getUser_id(), questCerti.getQuest_id());
    //         if (!awardedBadges.isEmpty()){
    //             result.put("badgeAwarded", true);
    //             result.put("awardedBadges", awardedBadges);
    //         }
    //     }
    //     return result;
    // }

    

    public void rejectQuest(int quest_user_id){
        adminQuestMapper.updateQuestUserReject(quest_user_id);

        //xp / 레벨 낮추는 과정
    }
}
