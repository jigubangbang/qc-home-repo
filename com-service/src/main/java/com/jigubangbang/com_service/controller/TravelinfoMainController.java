package com.jigubangbang.com_service.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jigubangbang.com_service.model.TravelInfoListResponse;
import com.jigubangbang.com_service.service.TravelinfoService;

@RestController
@RequestMapping("/com/travelinfo")
public class TravelinfoMainController {
    @Autowired
    private TravelinfoService travelinfoService;

    @GetMapping("/list")
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

    @GetMapping("/likes")
    public ResponseEntity<Map<String, Object>> getLikedTravelInfos() {
        // 인증 못하면
        // if (authentication == null || !authentication.isAuthenticated()) {
        //     return ResponseEntity.ok(Map.of("likedTravelInfoIds", List.of()));
        // }

        // String userId = authentication.getName();
        //#NeedToChange
        String userId = "aaa";
        List<Long> likedTravelInfoIds = travelinfoService.getLikedTravelInfoIds(userId);
        
        return ResponseEntity.ok(Map.of("likedTravelInfoIds", likedTravelInfoIds));
    }

    @PostMapping("/like/{travelInfoId}")
    public ResponseEntity<Map<String, Object>> addLike(
            @PathVariable Long travelInfoId) {
        
        // if (authentication == null || !authentication.isAuthenticated()) {
        //     return ResponseEntity.status(401).body(Map.of("error", "로그인이 필요합니다."));
        // }

        // String userId = authentication.getName();
        //#NeedToChange
        String userId = "aaa";
        try {
            travelinfoService.addLike(travelInfoId, userId);
            return ResponseEntity.ok(Map.of("success", true, "message", "좋아요가 추가되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/like/{travelInfoId}")
    public ResponseEntity<Map<String, Object>> removeLike(
            @PathVariable Long travelInfoId) {
        
        // if (authentication == null || !authentication.isAuthenticated()) {
        //     return ResponseEntity.status(401).body(Map.of("error", "로그인이 필요합니다."));
        // }

        //String userId = authentication.getName();
        //#NeedToChange
        String userId = "aaa";
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

    @PostMapping("/{travelInfoId}/join")
    public ResponseEntity<Map<String, Object>> joinTravelInfo(
            @PathVariable Long travelInfoId) {
        
        // if (authentication == null || !authentication.isAuthenticated()) {
        //     return ResponseEntity.status(401).body(Map.of("error", "로그인이 필요합니다."));
        // }

        // String userId = authentication.getName();
        //#NeedToChange
        String userId = "aaa";
        try {
            travelinfoService.joinTravelInfo(travelInfoId, userId);
            return ResponseEntity.ok(Map.of("success", true, "message", "여행정보 참여가 완료되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/joined-chats")
    public ResponseEntity<Map<String, Object>> getJoinedTravelInfos() {
        // if (authentication == null || !authentication.isAuthenticated()) {
        //     return ResponseEntity.ok(Map.of("joinedChatIds", List.of()));
        // }

        // String userId = authentication.getName();
        //#NeedToChange
        String userId = "aaa";
        List<Long> joinedChatIds = travelinfoService.getJoinedTravelInfoIds(userId);
        
        return ResponseEntity.ok(Map.of("joinedChatIds", joinedChatIds));
    }

}
