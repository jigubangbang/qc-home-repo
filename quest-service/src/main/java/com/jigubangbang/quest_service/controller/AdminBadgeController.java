package com.jigubangbang.quest_service.controller;

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
import org.springframework.web.multipart.MultipartFile;

import com.jigubangbang.quest_service.model.BadgeCreateRequest;
import com.jigubangbang.quest_service.model.BadgeIdCheckResponse;
import com.jigubangbang.quest_service.model.BadgePublicModalDto;
import com.jigubangbang.quest_service.model.BadgeUpdateRequest;
import com.jigubangbang.quest_service.service.BadgeService;
import com.jigubangbang.quest_service.service.S3Service;

import jakarta.annotation.Resource;


@RestController
@RequestMapping("/admin-quests")
public class AdminBadgeController {
    @Autowired
    private BadgeService badgeService;

    @Resource
    private S3Service s3Service;
    
    @GetMapping("/badges")
    public ResponseEntity<Map<String, Object>> getAdminBadgeList(){
        Map<String, Object> result = badgeService.getAdminBadgeList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/badges/{badge_id}")
    public ResponseEntity<BadgePublicModalDto> getAdminBadgeDetail(
        @PathVariable("badge_id") int badge_id
    ){
        BadgePublicModalDto badge = badgeService.getBadgePublicModal(badge_id);
        if (badge == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(badge);
    }

    //수정 form 보여주기
    @GetMapping("/badges/{badge_id}/modify")
    public ResponseEntity<Map<String, Object>> getBadgeDetail(@PathVariable int badge_id) {
        try {
            Map<String, Object> result = badgeService.getBadgeDetail(badge_id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "뱃지 상세 정보 조회 실패: " + e.getMessage()));
        }
    }

    @PutMapping("/badges/{badge_id}")
    public ResponseEntity<Map<String, Object>> updateBadge(
        @PathVariable int badge_id,
        @RequestBody BadgeUpdateRequest request) {
        
        try {
            badgeService.updateBadge(badge_id, request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "뱃지가 성공적으로 수정되었습니다");
            response.put("badge_id", badge_id);
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "뱃지 수정 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }

    @PostMapping("/upload-image/{type}")
    public ResponseEntity<Map<String, Object>> uploadAdminImage(
        @RequestParam("file") MultipartFile file, 
        @PathVariable("type") String fileType) {
        try {
            String s3Url = null;
            if (fileType.equals("badge")){
                s3Url = s3Service.uploadFile(file, "badge-images/");
            }else{
                s3Url = s3Service.uploadFile(file, "quest-images/");
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Uploaded image successfully");
            response.put("imageUrl", s3Url);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Upload failed"));
        }
    }
    

    @PostMapping("/badges")
    public ResponseEntity<?> createBadge(@RequestBody BadgeCreateRequest request) {
        try {
            // 난이도 유효성 검사 (1: Easy, 2: Normal, 3: Hard)
            if (request.getDifficulty() < 1 || request.getDifficulty() > 3) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "난이도는 1(Easy), 2(Normal), 3(Hard) 중 하나여야 합니다."));
            }

            // ID 중복 확인
            if (badgeService.existsBadgeById(request.getId())) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "이미 사용 중인 ID입니다."));
            }

            int badgeId = badgeService.createBadge(request);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "뱃지가 성공적으로 생성되었습니다.",
                "badgeId", badgeId
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                .body(Map.of("error", "뱃지 생성 중 오류가 발생했습니다: " + e.getMessage()));
        }
    }

    @GetMapping("/badges/check-id/{badgeId}")
    public ResponseEntity<BadgeIdCheckResponse> checkBadgeIdAvailability(@PathVariable int badgeId) {
        try {
            BadgeIdCheckResponse response = badgeService.checkBadgeIdAvailability(badgeId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 에러 발생 시 사용 불가능으로 처리
            BadgeIdCheckResponse errorResponse = new BadgeIdCheckResponse();
            errorResponse.setAvailable(false);
            errorResponse.setSuggestedId(null);
            errorResponse.setMessage("ID 확인 중 오류가 발생했습니다.");
            return ResponseEntity.ok(errorResponse);
        }
    }

    @DeleteMapping("/badges/{badge_id}")
    public ResponseEntity<Map<String, Object>> deleteBadge(@PathVariable("badge_id") int badge_id){
        Map<String, Object> response = new HashMap<>();

        badgeService.deleteBadge(badge_id);
        response.put("success", true);
        response.put("message", "뱃지가 삭제되었습니다");
        return ResponseEntity.ok(response);
    }
}
