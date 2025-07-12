package com.jigubangbang.quest_service.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jigubangbang.quest_service.model.QuestCerti;
import com.jigubangbang.quest_service.model.QuestModalDto;
import com.jigubangbang.quest_service.model.QuestUserDto;
import com.jigubangbang.quest_service.model.UserJourneyDto;
import com.jigubangbang.quest_service.service.S3Service;
import com.jigubangbang.quest_service.service.UserQuestService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user-quests")
public class UserQuestController {
    @Autowired
    private UserQuestService userQuestService;

    @Resource
    private S3Service s3Service;

    //quest 조회
    @GetMapping("/detail/{quest_id}")
    public ResponseEntity<QuestModalDto> getQuestDetail(
        @PathVariable("quest_id") int quest_id,
        @RequestHeader("User-Id") String userId
    ) {
        QuestModalDto quest = userQuestService.getQuestModalById(userId, quest_id);
        if (quest == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(quest);
    }

     @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getUserQuestList(
        @RequestParam(defaultValue="1") int pageNum,
        @RequestParam(defaultValue="0") int category,
        @RequestParam(required=false) String sortOption,
        @RequestParam(required=false) String difficulty,
        @RequestParam(required=false) String search,
        @RequestParam(defaultValue = "10") int limit,
        @RequestHeader("User-Id") String userId
    ){
        Map<String, Object> result = userQuestService.getUserQuests(userId, pageNum, category, sortOption, difficulty, search, limit);
        return ResponseEntity.ok(result);
    }

    //퀘스트 도전
     @PostMapping("/challenge/{quest_id}")
    public ResponseEntity<Map<String, Object>> challengeQuest(
        @PathVariable("quest_id") int quest_id,
        @RequestHeader("User-Id") String userId
    ){
        Map<String, Object> response = new HashMap<>();

        userQuestService.challengeQuest(userId, quest_id);
        response.put("success", true);
        response.put("message", "퀘스트 도전 완료");
        return ResponseEntity.ok(response);
    }
    //퀘스트 도전
     @PostMapping("/{quest_user_id}/season-end")
    public ResponseEntity<Map<String, Object>> seasonEndQuest(
        @PathVariable("quest_user_id") int quest_user_id
    ){
        Map<String, Object> response = new HashMap<>();
        userQuestService.seasonEndQuest(quest_user_id);
        response.put("success", true);
        response.put("message", "퀘스트 도전 완료");
        return ResponseEntity.ok(response);
    }


    //재도전
     @PostMapping("/reChallenge/{quest_id}")
    public ResponseEntity<Map<String, Object>> reChallengeQuest(
        @PathVariable("quest_id") int quest_id,
        @RequestHeader("User-Id") String userId
    ){
        Map<String, Object> response = new HashMap<>();

        //중복 체크
        if (userQuestService.countUserQuest(userId, quest_id)>0){
            response.put("success", false);
            response.put("message", "이미 도전 중인 퀘스트입니다");
            return ResponseEntity.ok(response);
        }

        userQuestService.reChallengeQuest(userId, quest_id);
        response.put("success", true);
        response.put("message", "퀘스트 재도전 완료");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/journey")
    public ResponseEntity<UserJourneyDto> getUserJourney(
        @RequestHeader("User-Id") String userId
    ){
        UserJourneyDto journey = userQuestService.getUserJourney(userId);
        return ResponseEntity.ok(journey);
    }

    //퀘스트 목록 조회
    @GetMapping("/detail")
    public ResponseEntity<List<QuestUserDto>> getUserQuestList(
        @RequestParam(required=false) String user_id,
        @RequestParam(required=false) String order,
        @RequestParam(required=false) String status,
        @RequestHeader("User-Id") String userId
    ){
        //user_id가 null 값이라면
        if (user_id == null || user_id.isEmpty()){
            //현재 로그인한 유저의 아이디
            user_id = userId;
        }

        String orderColumn = null;
        if ("xp".equals(order)) {
            orderColumn = "xp";
        } else if ("started_at".equals(order)) {
            orderColumn = "started_at";
        } else if ("completed_at".equals(order)) {
            orderColumn = "completed_at";
        }

        String statusFilter = null;
        if ("IN_PROGRESS".equals(status) || "COMPLETED".equals(status) || "GIVEN_UP".equals(status)) {
            statusFilter = status;
        }

        List<QuestUserDto> questList = userQuestService.getUserQuestList(user_id, orderColumn, statusFilter);
        return ResponseEntity.ok(questList);
    }
    //퀘스트 인증 조회
    @GetMapping("/certificate/{quest_user_id}")
    public ResponseEntity<QuestCerti> getQuestCerti(
        @PathVariable("quest_user_id") int quest_user_id
    ){
        QuestCerti questCerti = userQuestService.getQuestCerti(quest_user_id);
        if(questCerti == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(questCerti);
    }
    
    // 이미지 업로드 엔드포인트
    @PostMapping("/{quest_user_id}/upload-image")
    public ResponseEntity<Map<String, Object>> uploadQuestImage(
        @RequestParam("file") MultipartFile file, 
        @PathVariable("quest_user_id") int quest_user_id) {
        try {
            String s3Url = s3Service.uploadFile(file, "quest-images/");
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Uploaded quest image successfully");
            response.put("imageUrl", s3Url);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Upload failed"));
        }
    }

    //퀘스트 완료 제출
    @PostMapping("/{quest_user_id}/complete")
    public ResponseEntity<Map<String, Object>> completeQuest(
        @PathVariable("quest_user_id") int quest_user_id,
        @RequestBody QuestCerti completeRequest)
    {
        Map<String, Object> response = userQuestService.completeQuest(quest_user_id, completeRequest);
        return ResponseEntity.ok(response);    
    }

    //퀘스트 포기
    @PostMapping("/{quest_user_id}/abandon")
    public ResponseEntity<Map<String, Object>> abandonQuest(
        @PathVariable("quest_user_id") int quest_user_id)
    {
        Map<String, Object> response = new HashMap<>();

        userQuestService.abandonQuest(quest_user_id);
        response.put("success", true);
        response.put("message", "퀘스트를 포기했습니다.");
        return ResponseEntity.ok(response);
    }
}


