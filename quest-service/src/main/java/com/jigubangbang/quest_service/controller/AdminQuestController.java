package com.jigubangbang.quest_service.controller;


import java.util.HashMap;
import java.util.List;
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

import com.jigubangbang.quest_service.model.AdminQuestDetailDto;
import com.jigubangbang.quest_service.model.AdminQuestDto;
import com.jigubangbang.quest_service.model.QuestDto;
import com.jigubangbang.quest_service.service.AdminQuestService;

@RestController
@RequestMapping("/admin-quests")
public class AdminQuestController {
    @Autowired
    private AdminQuestService adminQuestService;

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getQuestList(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "default") String sortOption,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "all") String status) {
        
        try {
            Map<String, Object> result = adminQuestService.getQuestList(pageNum, search, sortOption, limit, status);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "퀘스트 목록을 불러오는 중 오류가 발생했습니다.");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/detail/{quest_id}")
    public ResponseEntity<Map<String, Object>> getQuestDetail(@PathVariable int quest_id) {
        try {
            AdminQuestDetailDto questDetail = adminQuestService.getQuestDetail(quest_id);
            
            if (questDetail == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "퀘스트를 찾을 수 없습니다.");
                errorResponse.put("quest_id", quest_id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("questDetail", questDetail);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "퀘스트 상세 정보를 불러오는 중 오류가 발생했습니다.");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/detail/{quest_id}/users")
    public ResponseEntity<Map<String, Object>> getQuestUsers(
            @PathVariable int quest_id,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "5") int limit) {
        
        try {
            Map<String, Object> result = adminQuestService.getQuestUsers(quest_id, pageNum, limit);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "퀘스트 참여자 목록을 불러오는 중 오류가 발생했습니다.");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    
    //퀘스트 생성
    @PostMapping("/quest")
    public ResponseEntity<QuestDto> createQuest(@RequestBody QuestDto quest){
        QuestDto createdQuest = adminQuestService.createQuest(quest);
        return ResponseEntity.ok(createdQuest);
    }
    
    //퀘스트 수정
    @PutMapping("/quest/{quest_id}")
    public ResponseEntity<QuestDto> updateQuest(@PathVariable int quest_id, @RequestBody QuestDto quest){
        QuestDto updatedQuest = adminQuestService.updateQuest(quest_id, quest);
        return ResponseEntity.ok(updatedQuest);
    }

    //퀘스트 삭제
    @DeleteMapping("/quest/{quest_id}")
    public ResponseEntity<Void> deleteQuest(@PathVariable int quest_id){
        adminQuestService.deleteQuest(quest_id);
        return ResponseEntity.noContent().build();
    }

    //퀘스트 인증 목록 조회 (모든 퀘스트)
    // @GetMapping("/certi-list")
    // public Map<String, Object> getAdminQuestList(
    //     @RequestParam(defaultValue="1") int pageNum,
    //     @RequestParam(required=false) String sortOption,
    //     @RequestParam(required=false) String status
    // ){
    //     return adminQuestService.getQuestCertiList(pageNum, sortOption, status); 
    // }

    //퀘스트 인증 상세 조회
    
    //reject -> 삭제로
    @PutMapping("/quests-certi/{quest_user_id}/reject")
    public ResponseEntity<Map<String, Object>> rejectQuest(@PathVariable int quest_user_id){
        Map<String, Object> response = new HashMap<>();
        
        adminQuestService.rejectQuest(quest_user_id);
        response.put("success", true);
        response.put("message", "퀘스트 인증 취소 완료");
        return ResponseEntity.ok(response);
    }
}
