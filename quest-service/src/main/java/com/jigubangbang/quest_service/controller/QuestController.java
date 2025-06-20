package com.jigubangbang.quest_service.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jigubangbang.quest_service.enums.Difficulty;
import com.jigubangbang.quest_service.service.QuestService;

@RestController
@RequestMapping("/api/quests")
public class QuestController {
    @Autowired
    private QuestService questService;
    
    @GetMapping
    public Map<String, Object> getQuestList(
        @RequestParam(defaultValue="1") int pageNum,
        @RequestParam(defaultValue="0") int category,
        @RequestParam(required=false) String sortOption,
        @RequestParam(required=false) Difficulty difficulty
    ){
        return questService.getQuests(pageNum, category, sortOption, difficulty);
    }
}
