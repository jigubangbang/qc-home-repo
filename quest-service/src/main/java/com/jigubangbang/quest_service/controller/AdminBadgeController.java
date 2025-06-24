package com.jigubangbang.quest_service.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jigubangbang.quest_service.model.BadgeDto;
import com.jigubangbang.quest_service.service.BadgeService;

@RestController
@RequestMapping("/admin-quests")
public class AdminBadgeController {
    @Autowired
    private BadgeService badgeService;
    
    @GetMapping("/badges")
    public ResponseEntity<Map<String, Object>> getAdminBadgeList(){
        Map<String, Object> result = badgeService.getAdminBadgeList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/badges/{badge_id}")
    public ResponseEntity<BadgeDto> getAdminBadgeDetail(
        @PathVariable("badge_id") int badge_id
    ){
        BadgeDto badge = badgeService.getBadgeById(badge_id);
        if(badge == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(badge);
    }

    @PostMapping("/badges")
    public ResponseEntity<Map<String, Object>> createBadge(@RequestBody BadgeDto badge){
        Map<String, Object> response = new HashMap<>();
        
        BadgeDto createdBadge = badgeService.createBadge(badge);
        response.put("success", true);
        response.put("message", "뱃지가 생성되었습니다");
        response.put("data", createdBadge);
        return ResponseEntity.ok(response);
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
