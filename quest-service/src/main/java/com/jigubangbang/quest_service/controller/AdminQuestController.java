package com.jigubangbang.quest_service.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @DeleteMapping("/{quest_id}")
    public ResponseEntity<Void> deleteQuest(@PathVariable int quest_id){
        adminQuestService.deleteQuest(quest_id);
        return ResponseEntity.noContent().build();
    }

    //퀘스트 인증 목록 조회 (승인 대기)
    @GetMapping("/list")
    public Map<String, Object> getAdminQuestList(
        @RequestParam(defaultValue="1") int pageNum,
        @RequestParam(required=false) String sortOption,
        @RequestParam(required=false) String status
    ){
        return adminQuestService.getQuestCertiList(pageNum, sortOption, status); 
    }

    //퀘스트 인증 상세 조회
    @GetMapping("/{quest_user_id}")
    public Map<String, Object> getAdminQuestDetail(@PathVariable int quest_user_id){
        return adminQuestService.getQuestCerti(quest_user_id);
    }
    
    @PutMapping("/{quest_user_id}/approve")
    public ResponseEntity<Map<String, Object>> approveQuest(@PathVariable int quest_user_id){
        try {
            Map<String, Object> result = adminQuestService.approveQuest(quest_user_id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "퀘스트 승인 완료");
            response.put("badgeAwarded", result.get("badgeAwarded"));
            response.put("awardedBadges", result.get("awardedBadges"));
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "퀘스트 승인 중 오류가 발생했습니다: " + e.getMessage());
            errorResponse.put("badgeAwarded", false);
            errorResponse.put("awardedBadges", new ArrayList<>());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @PutMapping("/{quest_user_id}/reject")
    public ResponseEntity<Map<String, Object>> rejectQuest(@PathVariable int quest_user_id){
        Map<String, Object> response = new HashMap<>();
        
        adminQuestService.rejectQuest(quest_user_id);
        response.put("success", true);
        response.put("message", "퀘스트 거절 완료");
        return ResponseEntity.ok(response);
    }
}
