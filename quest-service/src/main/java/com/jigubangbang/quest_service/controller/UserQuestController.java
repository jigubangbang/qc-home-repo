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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jigubangbang.quest_service.chat_service.NotificationServiceClient;
import com.jigubangbang.quest_service.model.QuestCerti;
import com.jigubangbang.quest_service.model.QuestModalDto;
import com.jigubangbang.quest_service.model.QuestUserDto;
import com.jigubangbang.quest_service.model.UserJourneyDto;
import com.jigubangbang.quest_service.model.chat_service.BadgeNotificationRequestDto;
import com.jigubangbang.quest_service.service.S3Service;
import com.jigubangbang.quest_service.service.UserQuestService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user-quests")
public class UserQuestController {
    @Autowired
    private UserQuestService userQuestService;

    @Autowired
    private NotificationServiceClient notificationClient;

    @Resource
    private S3Service s3Service;

    //quest 조회
    @GetMapping("/detail/{quest_id}")
    public ResponseEntity<QuestModalDto> getQuestDetail(@PathVariable("quest_id") int quest_id) {
         //#NeedToChange
        //session에서 user id 받아오기
        String current_user_id = "aaa";

        QuestModalDto quest = userQuestService.getQuestModalById(current_user_id, quest_id);
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
        @RequestParam(defaultValue = "10") int limit
    ){
        //#NeedToChange 실제 로그인한 사용자 ID 가져오기
        String userId = "aaa"; // 현재는 하드코딩, 실제로는 JWT에서 추출
        
        Map<String, Object> result = userQuestService.getUserQuests(userId, pageNum, category, sortOption, difficulty, search, limit);
        return ResponseEntity.ok(result);
    }

    //퀘스트 도전
    @PostMapping("/challenge/{quest_id}")
    public ResponseEntity<Map<String, Object>> challengeQuest(
        HttpServletRequest request,
        @PathVariable("quest_id") int quest_id
    ){
        //#NeedToChange
        //session에서 user id 받아오기
        String user_id = "aaa";
        Map<String, Object> response = new HashMap<>();

        //중복 체크
        if (userQuestService.countUserQuest(user_id, quest_id)>0){
            response.put("success", false);
            response.put("message", "이미 도전 중인 퀘스트입니다");
            return ResponseEntity.ok(response);
        }


        userQuestService.challengeQuest(user_id, quest_id);
        response.put("success", true);
        response.put("message", "퀘스트 도전 완료");
        return ResponseEntity.ok(response);
    }

    //퀘스트 도전
    @PostMapping("/{quest_user_id}/season-end")
    public ResponseEntity<Map<String, Object>> seasonEndQuest(
        HttpServletRequest request,
        @PathVariable("quest_user_id") int quest_user_id
    ){
        //#NeedToChange
        //session에서 user id 받아오기
        Map<String, Object> response = new HashMap<>();
        userQuestService.seasonEndQuest(quest_user_id);
        response.put("success", true);
        response.put("message", "퀘스트 도전 완료");
        return ResponseEntity.ok(response);
    }

    //재도전
    @PostMapping("/reChallenge/{quest_id}")
    public ResponseEntity<Map<String, Object>> reChallengeQuest(
        HttpServletRequest request,
        @PathVariable("quest_id") int quest_id
    ){
        //#NeedToChange
        //session에서 user id 받아오기
        String user_id = "aaa";
        Map<String, Object> response = new HashMap<>();

        //중복 체크
        if (userQuestService.countUserQuest(user_id, quest_id)>0){
            response.put("success", false);
            response.put("message", "이미 도전 중인 퀘스트입니다");
            return ResponseEntity.ok(response);
        }

        userQuestService.reChallengeQuest(user_id, quest_id);
        response.put("success", true);
        response.put("message", "퀘스트 재도전 완료");
        return ResponseEntity.ok(response);
    }

    //My Quest Journey stats id, nickname, profile, 퀘스트 완료 수, 레벨
    //#NeedToChange 
    @GetMapping("/journey")
    public ResponseEntity<UserJourneyDto> getUserJourney(HttpServletRequest request){
        //#NeedToChange
        String user_id = "aaa";
        UserJourneyDto journey = userQuestService.getUserJourney(user_id);
        return ResponseEntity.ok(journey);
    }

    //퀘스트 목록 조회
    @GetMapping("/detail")
    public ResponseEntity<List<QuestUserDto>> getUserQuestList(HttpServletRequest  request,
        @RequestParam(required=false) String user_id,
        @RequestParam(required=false) String order,
        @RequestParam(required=false) String status
    ){
        //#NeedToChange 
        //user_id가 null 값이라면
        if (user_id == null || user_id.isEmpty()){
            //현재 로그인한 유저의 아이디
            user_id = "aaa";
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
        try {
            Map<String, Object> response = userQuestService.completeQuest(quest_user_id, completeRequest);
            
            // 예시 (테스트용 하드코딩) =================================================
            if (response.get("success") != null && (Boolean) response.get("success")) {
                    BadgeNotificationRequestDto request = BadgeNotificationRequestDto.builder()
                        .userId(String.valueOf(quest_user_id))
                        .badgeName("지하탐험가")
                        .message(null)
                        .badgeId(24)
                        .relatedUrl("/quests/badges/24")
                        .senderId("SYSTEM") // 시스템
                        .senderProfileImage(null)
                        .build();
                    try {
                        ResponseEntity<Map<String, Object>> notificationResponse = notificationClient.createBadgeEarnedNotification(request);
                        System.out.println("[UserQuestController] 뱃지 알림 발송 성공: " + notificationResponse.getBody());
                        
                        // 알림 발송 성공 정보를 응답에 추가
                        response.put("notificationSent", true);
                        response.put("badgeName", "테스트 뱃지");
                        
                    } catch (Exception notificationError) {
                        System.out.println("[UserQuestController] 뱃지 알림 발송 실패: " + notificationError.getMessage());
                        
                        // 알림 실패해도 퀘스트 완료는 성공으로 처리
                        response.put("notificationSent", false);
                        response.put("notificationError", notificationError.getMessage());
                    }
                // ====================================================================
                
                }
            return ResponseEntity.ok(response);    
        } catch (Exception e) {
            System.out.println("[UserQuestController] 퀘스트 완료 처리 실패: " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "퀘스트 완료 처리에 실패했습니다: " + e.getMessage()
            ));
        }
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
