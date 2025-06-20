package com.jigubangbang.quest_service.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jigubangbang.quest_service.enums.Difficulty;
import com.jigubangbang.quest_service.model.QuestListDto;
import com.jigubangbang.quest_service.repository.QuestMapper;

@Service
public class QuestService {
    @Autowired
    private QuestMapper questMapper;

    public Map<String, Object> getQuests(int pageNum, int category, String sortOption, Difficulty difficulty){
        int questPerPage = 20;
        int offset = (pageNum-1)*questPerPage;

        Map<String, Object> params = Map.of("offset", offset, "questPerPage", questPerPage, "sortOption", sortOption);
        List<QuestListDto> quests = questMapper.getQuests(params);
        int totalCount = questMapper.countQuests();
        int pageCount = (int) Math.ceil((double) totalCount/questPerPage);

        Map<String, Object> result = new HashMap<>();
        result.put("quests", quests);
        result.put("pageCount", pageCount);
        result.put("totalCount", totalCount);
        
        return result;
    }
}
