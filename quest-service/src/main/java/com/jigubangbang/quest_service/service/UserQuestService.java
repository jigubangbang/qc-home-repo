package com.jigubangbang.quest_service.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jigubangbang.quest_service.model.QuestCerti;
import com.jigubangbang.quest_service.model.QuestImageDto;
import com.jigubangbang.quest_service.model.QuestUserDto;
import com.jigubangbang.quest_service.model.UserJourneyDto;
import com.jigubangbang.quest_service.repository.UserQuestMapper;

@Service
public class UserQuestService {
    @Autowired
    private UserQuestMapper userQuestMapper;

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

    public void completeQuest(int quest_user_id, QuestCerti request){
        userQuestMapper.updateQuestUserPending(quest_user_id);

        String type = userQuestMapper.getQuestType(quest_user_id);

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

        //#NeedToChange
        //CHECK일 경우 검증..
    }


    public void abandonQuest(int quest_user_id){
        userQuestMapper.updateQuestUserAbandon(quest_user_id);
    }
}
