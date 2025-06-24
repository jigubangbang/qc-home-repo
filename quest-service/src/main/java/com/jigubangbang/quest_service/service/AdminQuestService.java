package com.jigubangbang.quest_service.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jigubangbang.quest_service.model.QuestCerti;
import com.jigubangbang.quest_service.model.QuestDto;
import com.jigubangbang.quest_service.repository.AdminQuestMapper;
import com.jigubangbang.quest_service.repository.QuestMapper;

@Service
public class AdminQuestService {
    @Autowired
    private AdminQuestMapper adminQuestMapper;

    @Autowired
    private QuestMapper questMapper;

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

    public Map<String, Object> approveQuest(int quest_user_id){
        Map<String, Object> result = new HashMap<>();
        result.put("badgeAwarded", false);
        result.put("awardedBadges", new ArrayList<>());

        adminQuestMapper.updateQuestUserApprove(quest_user_id);

        QuestCerti questCerti = adminQuestMapper.getQuestCerti(quest_user_id);
        QuestDto quest = questMapper.selectQuestById(questCerti.getQuest_id());
        
        if (quest != null && quest.getXp() > 0 ){
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", questCerti.getUser_id());
            params.put("xp", quest.getXp());
            adminQuestMapper.updateUserXp(params);
        }

        if (questCerti != null){
            List<Integer> awardedBadges = checkAndAwardBadges(questCerti.getUser_id(), questCerti.getQuest_id());
            if (!awardedBadges.isEmpty()){
                result.put("badgeAwarded", true);
                result.put("awardedBadges", awardedBadges);
            }
        }
        return result;
    }

    private List<Integer> checkAndAwardBadges(String user_id, int completed_quest_id){
        List<Integer> awardedBadges = new ArrayList<>();

        List<Integer> relatedBadgeIds = adminQuestMapper.getBadgeIdsByQuestId(completed_quest_id);
        
        for (int badge_id : relatedBadgeIds){
            List<Integer> requiredQuestIds = adminQuestMapper.getRequiredQuestsByBadgeId(badge_id);
            
            List<Integer> completedQuestIds = adminQuestMapper.getCompletedQuestsByUserAndBadge(user_id, badge_id);

            if (completedQuestIds.containsAll(requiredQuestIds)){
                boolean alreadyHasBadge = adminQuestMapper.checkUserHasBadge(user_id, badge_id);

                if(!alreadyHasBadge){
                    Map<String, Object> badgeParams = new HashMap<>();
                    badgeParams.put("user_id", user_id);
                    badgeParams.put("badge_id", badge_id);
                    adminQuestMapper.insertUserBadge(badgeParams);

                    awardedBadges.add(badge_id);
                }
            }
        }

        return awardedBadges;
    }

    public void rejectQuest(int quest_user_id){
        adminQuestMapper.updateQuestUserReject(quest_user_id);
    }
}
