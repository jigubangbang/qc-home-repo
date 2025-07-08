package com.jigubangbang.quest_service.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.jigubangbang.quest_service.model.BadgeDto;
import com.jigubangbang.quest_service.model.BadgeModalDto;
import com.jigubangbang.quest_service.service.BadgeService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user-quests")
public class UserBadgeController {
    @Autowired
    private BadgeService badgeService;

    @GetMapping("/badges/{badge_id}")
    public ResponseEntity<BadgeModalDto> getBadgeModal(
        @PathVariable("badge_id") int badge_id
    ){
        //#NeedToChange
        String user_id = "aaa";

        BadgeModalDto badge = badgeService.getBadgeModal(badge_id, user_id);
        if (badge == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(badge);
    }


    @PostMapping("/badges/{badge_id}/pin")
    public ResponseEntity<Map<String, Object>> pinBadge(
        @PathVariable("badge_id") int badge_id,
        HttpServletRequest request
    ){
        //#NeedToChange
        String user_id = "aaa";
        Map<String, Object> response = new HashMap<>();

        boolean result = badgeService.pinBadge(user_id, badge_id);
        if(result){
            response.put("success", true);
            response.put("message", "뱃지 설정 완료");
        }else{
            response.put("success", false);
            response.put("message", "보유하지 않은 뱃지");
        }
        return ResponseEntity.ok(response);
    }

    //내 뱃지 목록
    @GetMapping("/badges")
    public ResponseEntity<Map<String, Object>> getMayBadges(HttpServletRequest request){
        //#NeedToChange
        String user_id = "aaa";
        Map<String, Object> result = badgeService.getUserBadgeInfo(user_id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/badges/my")
    public ResponseEntity<Map<String, Object>> getAwardedBadges(HttpServletRequest request){
        //#NeedToChange
        String user_id = "aaa";
        Map<String, Object> result = badgeService.getUserBadges(user_id);
        return ResponseEntity.ok(result);
    }
}
