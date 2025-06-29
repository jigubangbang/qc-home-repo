package com.jigubangbang.quest_service.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jigubangbang.quest_service.model.QuestDto;
import com.jigubangbang.quest_service.model.UserJourneyDto;
import com.jigubangbang.quest_service.service.QuestService;
import com.jigubangbang.quest_service.service.UserQuestService;

@RestController
@RequestMapping("/quests")
public class QuestController {
    @Autowired
    private QuestService questService;

    @Autowired
    private UserQuestService userQuestService;
    
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getQuestList(
        @RequestParam(defaultValue="1") int pageNum,
        @RequestParam(defaultValue="0") int category,
        @RequestParam(required=false) String sortOption,
        @RequestParam(required=false) String difficulty
    ){
        Map<String, Object> result = questService.getQuests(pageNum, category, sortOption, difficulty);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{quest_id}")
    public ResponseEntity<QuestDto> getQuestDetail(@PathVariable("quest_id") int quest_id) {
        QuestDto quest = questService.getQuestById(quest_id);
        if (quest == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(quest);
    }

    @GetMapping("/{quest_id}/participants")
    public ResponseEntity<Map<String, Object>> getQuestParticipants(
        @PathVariable("quest_id") int quest_id
    ){
        Map<String, Object> result = questService.getQuestParticipants(quest_id);
        return ResponseEntity.ok(result);
    }

    //사용자 xp/레벨 조회
    @GetMapping("/stats/{user_id}")
    public ResponseEntity<UserJourneyDto> getUserLevel(
        @PathVariable("user_id") String user_id
    ){
        UserJourneyDto userJourney = userQuestService.getUserJourney(user_id);
        if (userJourney == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userJourney);
    }

}
