package com.jigubangbang.com_service.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jigubangbang.com_service.model.TravelInfoListResponse;
import com.jigubangbang.com_service.model.TravelInfoRequestDto;
import com.jigubangbang.com_service.model.TravelInfoResponseDto;
import com.jigubangbang.com_service.service.TravelinfoService;

@RestController
@RequestMapping
public class TravelinfoMainController {
    @Autowired
    private TravelinfoService travelinfoService;

    @GetMapping("/com/travelinfo/list")
    public ResponseEntity<TravelInfoListResponse> getTravelInfoList(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(required = false) String themes,
            @RequestParam(defaultValue = "latest") String sortOption,
            @RequestParam(required = false) String search) {

        // 페이지 설정
        int pageSize = 10;
        int offset = (pageNum - 1) * pageSize;

        // 테마 파라미터 파싱
        List<Integer> themeList = parseCommaSeparatedIntegerString(themes);

        TravelInfoListResponse response = travelinfoService.getTravelInfoList(
                pageNum,
                pageSize,
                offset,
                themeList,
                sortOption,
                search
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user-com/travelinfo/likes")
    public ResponseEntity<Map<String, Object>> getLikedTravelInfos(
            @RequestHeader("User-Id") String userId) {
        List<Long> likedTravelInfoIds = travelinfoService.getLikedTravelInfoIds(userId);
        return ResponseEntity.ok(Map.of("likedTravelInfoIds", likedTravelInfoIds));
    }

    @PostMapping("/user-com/travelinfo/like/{travelInfoId}")
    public ResponseEntity<Map<String, Object>> addLike(
            @PathVariable Long travelInfoId,
            @RequestHeader("User-Id") String userId) {
        
        try {
            travelinfoService.addLike(travelInfoId, userId);
            return ResponseEntity.ok(Map.of("success", true, "message", "좋아요가 추가되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/user-com/travelinfo/like/{travelInfoId}")
    public ResponseEntity<Map<String, Object>> removeLike(
            @PathVariable Long travelInfoId,
            @RequestHeader("User-Id") String userId) {
        
        try {
            travelinfoService.removeLike(travelInfoId, userId);
            return ResponseEntity.ok(Map.of("success", true, "message", "좋아요가 제거되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    private List<Integer> parseCommaSeparatedIntegerString(String str) {
        if (str == null || str.trim().isEmpty()) {
            return List.of();
        }
        try {
            return List.of(str.split(","))
                    .stream()
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .toList();
        } catch (NumberFormatException e) {
            return List.of();
        }
    }

    @PostMapping("/user-com/travelinfo/{travelInfoId}/join")
    public ResponseEntity<Map<String, Object>> joinTravelInfo(
            @PathVariable Long travelInfoId,
            @RequestHeader("User-Id") String userId) {
        
        try {
            //isCreator false값 주기
            travelinfoService.joinTravelInfo(travelInfoId, userId, false);
            return ResponseEntity.ok(Map.of("success", true, "message", "여행정보 참여가 완료되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/user-com/travelinfo/joined-chats")
    public ResponseEntity<Map<String, Object>> getJoinedTravelInfos(
            @RequestHeader("User-Id") String userId) {
        
        List<Long> joinedChatIds = travelinfoService.getJoinedTravelInfoIds(userId);
        return ResponseEntity.ok(Map.of("joinedChatIds", joinedChatIds));
    }

    @GetMapping("/com/travelinfo/{id}")
    public ResponseEntity<Map<String, Object>> getTravelInfo(@PathVariable Long id) {
        try {
            TravelInfoResponseDto travelinfo = travelinfoService.getTravelInfoById(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("travelinfo", travelinfo);
            response.put("success", true);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "정보방을 찾을 수 없습니다.");
            
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/user-com/travelinfo")
    public ResponseEntity<Map<String, Object>> createTravelInfo(
            @RequestBody TravelInfoRequestDto requestDTO,
            @RequestHeader("User-Id") String userId) {
        try {
            requestDTO.setCreatorId(userId);
            Long travelInfoId = travelinfoService.createTravelInfo(requestDTO);
            
            Map<String, Object> response = new HashMap<>();
            response.put("travelInfoId", travelInfoId);
            response.put("success", true);
            response.put("message", "정보방이 성공적으로 생성되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("정보방 생성 중 예상치 못한 오류: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "정보방 생성에 실패했습니다: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PutMapping("/user-com/travelinfo/{id}")
    public ResponseEntity<Map<String, Object>> updateTravelInfo(
            @PathVariable Long id, 
            @RequestBody TravelInfoRequestDto requestDTO) {
        try {
            travelinfoService.updateTravelInfo(id, requestDTO);
            
            Map<String, Object> response = new HashMap<>();
            response.put("travelInfoId", id);
            response.put("success", true);
            response.put("message", "정보방이 성공적으로 수정되었습니다.");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "정보방 수정에 실패했습니다.");
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @DeleteMapping("/user-com/travelinfo/{travelinfoId}")
    public ResponseEntity<Map<String, Object>> deleteTravelInfo(
            @PathVariable Long travelinfoId,
            @RequestHeader("User-Id") String userId) {
        try {
            travelinfoService.deleteTravelInfo(travelinfoId, userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "정보방이 성공적으로 삭제되었습니다.");
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "삭제 권한이 없습니다.");
            
            return ResponseEntity.status(403).body(errorResponse);
            
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "정보방을 찾을 수 없습니다.");
            
            return ResponseEntity.status(404).body(errorResponse);
            
        } catch (Exception e) {
            System.err.println("정보방 삭제 중 예상치 못한 오류: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "정보방 삭제에 실패했습니다: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
