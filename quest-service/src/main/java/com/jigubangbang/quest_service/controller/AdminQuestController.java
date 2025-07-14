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

import com.jigubangbang.quest_service.chat_service.NotificationServiceClient;
import com.jigubangbang.quest_service.model.AdminQuestDetailDto;
import com.jigubangbang.quest_service.model.BadgeIdCheckResponse;
import com.jigubangbang.quest_service.model.QuestDto;
import com.jigubangbang.quest_service.model.chat_service.BadgeNotificationRequestDto;
import com.jigubangbang.quest_service.service.AdminQuestService;

@RestController
@RequestMapping("/admin-quests")
public class AdminQuestController {
    @Autowired
    private AdminQuestService adminQuestService;

    @Autowired
    private NotificationServiceClient notificationClient;

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

    @GetMapping("/detail/{quest_id}/badges")
    public ResponseEntity<Map<String, Object>> getQuestBadges(
            @PathVariable int quest_id) {
        
        try {
            Map<String, Object> result = adminQuestService.getQuestBadges(quest_id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "퀘스트 뱃지 목록을 불러오는 중 오류가 발생했습니다.");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/quests/check-quest-id/{questId}")
    public ResponseEntity<BadgeIdCheckResponse> checkQuestIdAvailability(@PathVariable int questId) {
        try {
            BadgeIdCheckResponse response = adminQuestService.checkBadgeIdAvailability(questId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            BadgeIdCheckResponse errorResponse = new BadgeIdCheckResponse();
            errorResponse.setAvailable(false);
            errorResponse.setSuggestedId(null);
            errorResponse.setMessage("ID 확인 중 오류가 발생했습니다.");
            return ResponseEntity.ok(errorResponse);
        }
    }
    
    //퀘스트 생성
    @PostMapping("/quests")
    public ResponseEntity<QuestDto> createQuest(@RequestBody QuestDto quest){
        QuestDto createdQuest = adminQuestService.createQuest(quest);
        return ResponseEntity.ok(createdQuest);
    }
    
    //퀘스트 수정
    @PutMapping("/quests/{quest_id}")
    public ResponseEntity<QuestDto> updateQuest(@PathVariable int quest_id, @RequestBody QuestDto quest){
        QuestDto updatedQuest = adminQuestService.updateQuest(quest_id, quest);
        return ResponseEntity.ok(updatedQuest);
    }

    //퀘스트 삭제
    @DeleteMapping("/quests/{quest_id}")
    public ResponseEntity<Void> deleteQuest(@PathVariable int quest_id){
        adminQuestService.deleteQuest(quest_id);
        return ResponseEntity.noContent().build();
    }

    
    @PutMapping("/quests-certi/{quest_user_id}/reject")
    public ResponseEntity<Map<String, Object>> rejectQuest(
        @PathVariable int quest_user_id,
        @RequestBody Map<String, Object> requestBody
    ){
        
        int xp = (Integer) requestBody.get("xp");
        String user_id = (String) requestBody.get("user_id");
        int quest_id = (Integer) requestBody.get("quest_id");
        Map<String, Object> serviceResult = adminQuestService.rejectQuest(quest_user_id, quest_id, xp, user_id);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "퀘스트 인증 취소 완료");

        // 배지 제거 시 알림 처리
        if (serviceResult.get("badgeRemoved") != null && (Boolean) serviceResult.get("badgeRemoved")) {
            System.out.println("제거된 배지가 있음 - 알림 처리 시작");
            
            Object removedBadgesObj = serviceResult.get("removedBadges");
            
            if (removedBadgesObj instanceof List<?>) {
                List<?> removedBadgesList = (List<?>) removedBadgesObj;
                System.out.println("제거된 배지 수: " + removedBadgesList.size());
                
                for (Object badgeInfoObj : removedBadgesList) {
                    if (badgeInfoObj instanceof Map<?, ?>) {
                        Map<?, ?> badgeInfoMap = (Map<?, ?>) badgeInfoObj;
                        
                        try {
                            String userId = String.valueOf(badgeInfoMap.get("userId"));
                            String badgeName = String.valueOf(badgeInfoMap.get("badgeName"));
                            Integer badgeId = (Integer) badgeInfoMap.get("badgeId");
                            
                            System.out.println("배지 제거 알림 데이터 - userId: " + userId + ", badgeName: " + badgeName + ", badgeId: " + badgeId);
                            
                            // 배지 제거 알림용 DTO (기존 BadgeNotificationRequestDto 재사용 또는 새로 만들기)

                            //여기서 이동을 문의로 하고 싶어요
                            //07-15
                            BadgeNotificationRequestDto notificationRequest = BadgeNotificationRequestDto.builder()
                                .userId(userId)
                                .badgeName(badgeName)
                                .message(null)
                                .badgeId(badgeId)
                                .relatedUrl("/my-quest/badge")
                                .senderId("jigubang")
                                .senderProfileImage(null)
                                .build();
                            
                            System.out.println("배지 제거 알림 요청 시작...");
                            
                            // 배지 제거 알림 API 호출 (새로운 엔드포인트 필요)
                            ResponseEntity<Map<String, Object>> notificationResponse = 
                                notificationClient.createBadgeRevokedNotification(notificationRequest);
                            
                            System.out.println("[AdminQuestController] 배지 제거 알림 발송 성공: " + notificationResponse.getBody());
                            
                        } catch (Exception notificationError) {
                            System.out.println("[AdminQuestController] 배지 제거 알림 발송 실패: " + notificationError.getMessage());
                            // 알림 실패해도 퀘스트 취소는 성공으로 처리
                        }
                    }
                }
                
                response.put("notificationProcessed", true);
            }
        } else {
            System.out.println("제거된 배지 없음");
        }
        
        System.out.println("=== 퀘스트 취소 완료 ===");


        return ResponseEntity.ok(response);
    }
}
