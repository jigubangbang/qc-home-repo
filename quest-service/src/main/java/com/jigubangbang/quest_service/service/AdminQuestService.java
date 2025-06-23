package com.jigubangbang.quest_service.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jigubangbang.quest_service.model.QuestDto;
import com.jigubangbang.quest_service.repository.AdminQuestMapper;

@Service
public class AdminQuestService {
    @Autowired
    private AdminQuestMapper adminQuestMapper;

    public QuestDto createQuest(QuestDto quest){
        adminQuestMapper.createQuest(quest);
        return quest;
    }

    public QuestDto updateQuest(int quest_id, QuestDto quest){
        Map<String, Object> params = new HashMap<>();
        params.put("quest_id", quest_id);
        params.put("quest", quest);
        adminQuestMapper.updateQuest(params);
        return quest;
    }

    public void deleteQuest(int quest_id){
        adminQuestMapper.deleteQuest(quest_id);
    }

}
