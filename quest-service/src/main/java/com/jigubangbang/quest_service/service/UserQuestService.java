package com.jigubangbang.quest_service.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jigubangbang.quest_service.model.BadgeDto;
import com.jigubangbang.quest_service.model.QuestCerti;
import com.jigubangbang.quest_service.model.QuestDto;
import com.jigubangbang.quest_service.model.QuestImageDto;
import com.jigubangbang.quest_service.model.QuestModalDto;
import com.jigubangbang.quest_service.model.QuestSimpleParticipantDto;
import com.jigubangbang.quest_service.model.QuestUserDto;
import com.jigubangbang.quest_service.model.UserJourneyDto;
import com.jigubangbang.quest_service.repository.AdminQuestMapper;
import com.jigubangbang.quest_service.repository.QuestMapper;
import com.jigubangbang.quest_service.repository.UserQuestMapper;

@Service
public class UserQuestService {
    @Autowired
    private UserQuestMapper userQuestMapper;

    @Autowired 
    private QuestMapper questMapper;
    
    @Autowired
    private AdminQuestMapper adminQuestMapper;

    public QuestModalDto getQuestModalById(String current_user_id, int quest_id){
        Map<String, Object> params = new HashMap<>();
        params.put("current_user_id", current_user_id);
        params.put("quest_id", quest_id);
        QuestModalDto questModal = userQuestMapper.getQuestModalById(params);

        if (questModal == null){
            return null;
        }

        List<BadgeDto> badges = userQuestMapper.getBadgesByQuestId(quest_id);
        questModal.setBadges(badges);

        List<QuestSimpleParticipantDto> inProgressUsers = userQuestMapper.getInProgressUsers(quest_id);
        questModal.setIn_progress_user(inProgressUsers);
        
        List<QuestSimpleParticipantDto> completedUsers = userQuestMapper.getCompletedUsers(quest_id);
        questModal.setCompleted_user(completedUsers);

        return questModal;
    }

    public int countUserQuest(String user_id, int quest_id){
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("quest_id", quest_id);
        return userQuestMapper.countUserQuest(params);
    }

    public void challengeQuest(String user_id, int quest_id){
        QuestUserDto questUser = new QuestUserDto();
        questUser.setUser_id(user_id);
        questUser.setQuest_id(quest_id);
        questUser.setStatus("IN_PROGRESS");
        questUser.setStarted_at(new Timestamp(System.currentTimeMillis()));
        userQuestMapper.insertQuestUser(questUser);
    }

    public void reChallengeQuest(String user_id, int quest_id){
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("quest_id", quest_id);
        userQuestMapper.reChallengeQuestUser(params);
    }

    //#NeedToChange
    public UserJourneyDto getUserJourney(String user_id){
        return userQuestMapper.getUserJourney(user_id);
    }

    public List<QuestUserDto> getUserQuestList(String user_id, String order, String status){
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("order", order);
        params.put("status", status);
        return userQuestMapper.getUserQuestList(params);
    }

    public QuestCerti getQuestCerti(int quest_user_id){
        QuestCerti questCerti = userQuestMapper.getQuestCerti(quest_user_id);
        if (questCerti != null){
            List<String> images = userQuestMapper.getQuestCertiImages(quest_user_id);
            questCerti.setImage_list(images);
        }
        return questCerti;
    }

    @Transactional
    public Map<String, Object> completeQuest(int quest_user_id, QuestCerti request){
        Map<String, Object> params2 = new HashMap<>();
        params2.put("quest_user_id", quest_user_id);
        params2.put("quest_description", request.getQuest_description());
        userQuestMapper.updateQuestUserCompleted(params2);

        String type = userQuestMapper.getQuestType(quest_user_id);

        //이미지 데이터 채우기
        if ("AUTH".equals(type) && request.getImage_list() != null && !request.getImage_list().isEmpty()) {
            List<QuestImageDto> images = new ArrayList<>();
            for (String imageUrl : request.getImage_list()) {
                QuestImageDto image = new QuestImageDto();
                image.setQuest_user_id(quest_user_id);
                image.setImage(imageUrl);
                images.add(image);
            }
            userQuestMapper.insertQuestImages(images);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("badgeAwarded", false);
        result.put("awardedBadges", new ArrayList<>());

        QuestCerti questCerti = userQuestMapper.getQuestCerti(quest_user_id);
        QuestDto quest = questMapper.selectQuestById(questCerti.getQuest_id());
        
        if (quest != null && quest.getXp() > 0 ){
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", questCerti.getUser_id());
            params.put("xp", quest.getXp());
            adminQuestMapper.updateUserXp(params);
            adminQuestMapper.updateUserLevel(questCerti.getUser_id());
        }

        if (questCerti != null){
            List<Integer> awardedBadges = checkAndAwardBadges(questCerti.getUser_id(), questCerti.getQuest_id());
            if (!awardedBadges.isEmpty()){
                result.put("badgeAwarded", true);
                result.put("awardedBadges", awardedBadges);
            }
        }
        return result;
        //#NeedToChange
    }

    private List<Integer> checkAndAwardBadges(String user_id, int completed_quest_id){
        List<Integer> awardedBadges = new ArrayList<>();

        List<Integer> relatedBadgeIds = adminQuestMapper.getBadgeIdsByQuestId(completed_quest_id);
        
        for (int badge_id : relatedBadgeIds){
            List<Integer> requiredQuestIds = adminQuestMapper.getRequiredQuestsByBadgeId(badge_id);
            
            List<Integer> completedQuestIds = adminQuestMapper.getCompletedQuestsByUserAndBadge(user_id, badge_id);

            if (completedQuestIds.containsAll(requiredQuestIds)){
                boolean alreadyHasBadge = false;

                if (adminQuestMapper.checkUserHasBadge(user_id, badge_id)>0){
                    alreadyHasBadge = true;
                }

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

    public void abandonQuest(int quest_user_id){
        userQuestMapper.updateQuestUserAbandon(quest_user_id);
    }

    public Map<String, Object> getUserQuests(String userId, int pageNum, int category, String sortOption, String difficulty, String search, int limit){
        Map<String, Object> params = new HashMap<>();
        params.put("user_id", userId); // 사용자 ID 추가
        
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
            params.put("search", search);
        }
        
        int offset = (pageNum-1)*limit;
        params.put("limit", limit);
        params.put("offset", offset);

        List<QuestDto> quests = userQuestMapper.getUserQuests(params);
        int totalCount = userQuestMapper.countUserQuests(params);
        int pageCount = (int) Math.ceil((double) totalCount/limit);

        Map<String, Object> result = new HashMap<>();
        result.put("quests", quests);
        result.put("pageCount", pageCount);
        result.put("totalCount", totalCount);
        result.put("currentPage", pageNum);
        return result;
    }
}
