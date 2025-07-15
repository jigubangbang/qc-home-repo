package com.jigubangbang.quest_service.controller;

import com.jigubangbang.quest_service.model.RankingDto;
import com.jigubangbang.quest_service.service.RankingService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quests")
public class RankingController {

    @Autowired
    private RankingService rankingService;

    @GetMapping("/weekly-quest")
    public ResponseEntity<RankingDto> getWeeklyQuestRanking() {
        RankingDto ranking = rankingService.getWeeklyQuestRanking();
        if (ranking != null) {
            return ResponseEntity.ok(ranking);
        } else {
            return ResponseEntity.ok(setNullRanking());
        }
    }


    @GetMapping("/weekly-level")
    public ResponseEntity<RankingDto> getWeeklyLevelRanking() {
        RankingDto ranking = rankingService.getWeeklyLevelRanking();
        if (ranking != null) {
            return ResponseEntity.ok(ranking);
        } else {
            return ResponseEntity.ok(setNullRanking());
        }
    }

    @GetMapping("/top-level")
    public ResponseEntity<RankingDto> getTopLevelRanking() {
        RankingDto ranking = rankingService.getTopLevelRanking();
        if (ranking != null) {
            return ResponseEntity.ok(ranking);
        } else {
            return ResponseEntity.ok(setNullRanking());
        }
    }

    @GetMapping("/top-quest")
    public ResponseEntity<RankingDto> getTopQuestRanking() {
        RankingDto ranking = rankingService.getTopQuestRanking();
        if (ranking != null) {
            return ResponseEntity.ok(ranking);
        } else {
            return ResponseEntity.ok(setNullRanking());
        }
    }

        
    public RankingDto setNullRanking(){
        RankingDto nullRanking = new RankingDto();
        nullRanking.setUser_id("No Person");
        nullRanking.setNickname("null");
        nullRanking.setProfile_image("/icons/common/default_profile.png");
        nullRanking.setLevel(0);
        nullRanking.setWeekly_quest_count(0);
        nullRanking.setWeekly_level_gain(0);
        nullRanking.setTotal_quest_count(0);
        return nullRanking;
    }

    @GetMapping("/rankings")
    public ResponseEntity<Map<String, Object>> getRankingList(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int limit,
        @RequestParam(required=false) String search
    ){
        Map<String, Object> result = rankingService.getRankingList(page, limit, search);
        return ResponseEntity.ok(result);
    }
}