package com.jigubangbang.quest_service.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jigubangbang.quest_service.model.QuestDto;
import com.jigubangbang.quest_service.model.QuestParticipantDto;
import com.jigubangbang.quest_service.repository.QuestMapper;

@Service
public class QuestService {
    @Autowired
    private QuestMapper questMapper;

    public Map<String, Object> getQuests(int pageNum, int category, String sortOption, String difficulty){
        //#NeedToChange
        int questPerPage = 100;
        int offset = (pageNum-1)*questPerPage;

        Map<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("questPerPage", questPerPage);

        if (category != 0){
            params.put("category", category);
        }
        if (sortOption != null && !sortOption.isEmpty()) {
            params.put("sortOption", sortOption);
        }
        if (difficulty != null && !difficulty.isEmpty()) {
            params.put("difficulty", difficulty);
        }

        List<QuestDto> quests = questMapper.getQuests(params);
        int totalCount = questMapper.countQuests();
        int pageCount = (int) Math.ceil((double) totalCount/questPerPage);

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
}
