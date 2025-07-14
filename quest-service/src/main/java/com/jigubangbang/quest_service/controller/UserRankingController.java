package com.jigubangbang.quest_service.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jigubangbang.quest_service.service.RankingService;

@RestController
@RequestMapping("/user-quests")
public class UserRankingController {
    @Autowired
    private RankingService rankingService;

    @GetMapping("/ranking/my")
    public ResponseEntity<Map<String, Object>> getMyRanking(
        @RequestHeader("User-Id") String userId
    ) {
        int totalMemeber = rankingService.getTotalMember();
        int myRank = rankingService.getRankingById(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("count", myRank);
        response.put("totalCount", totalMemeber);
        
        if (myRank != 0) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
