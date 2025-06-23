package com.jigubangbang.quest_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jigubangbang.quest_service.model.QuestDto;
import com.jigubangbang.quest_service.service.AdminQuestService;

@RestController
@RequestMapping("/admin-quests")
public class AdminQuestController {
    @Autowired
    private AdminQuestService adminQuestService;
    
    @PostMapping("/create")
    public ResponseEntity<QuestDto> createQuest(@RequestBody QuestDto quest){
        QuestDto createdQuest = adminQuestService.createQuest(quest);
        return ResponseEntity.ok(createdQuest);
    }

    @PutMapping("/{quest_id}")
    public ResponseEntity<QuestDto> updateQuest(@PathVariable int quest_id, @RequestBody QuestDto quest){
        QuestDto updatedQuest = adminQuestService.updateQuest(quest_id, quest);
        return ResponseEntity.ok(updatedQuest);
    }

    @DeleteMapping("/quest_id")
    public ResponseEntity<QuestDto> deleteQuest(@PathVariable int quest_id){
        adminQuestService.deleteQuest(quest_id);
        return ResponseEntity.noContent().build();
    }
}
