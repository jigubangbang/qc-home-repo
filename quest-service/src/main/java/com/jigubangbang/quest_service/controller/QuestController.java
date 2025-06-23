package com.jigubangbang.quest_service.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jigubangbang.quest_service.service.QuestService;

@RestController
@RequestMapping("/quests")
public class QuestController {
    @Autowired
    private QuestService questService;
    
    @GetMapping("/list")
    public Map<String, Object> getQuestList(
        @RequestParam(defaultValue="1") int pageNum,
        @RequestParam(defaultValue="0") int category,
        @RequestParam(required=false) String sortOption,
        @RequestParam(required=false) String difficulty
    ){
        return questService.getQuests(pageNum, category, sortOption, difficulty);
    }

    @GetMapping("/{quest_id}/participants")
    public Map<String, Object> getQuestParticipants(
        @PathVariable("quest_id") int quest_id
    ){
        return questService.getQuestParticipants(quest_id);
    }

}
