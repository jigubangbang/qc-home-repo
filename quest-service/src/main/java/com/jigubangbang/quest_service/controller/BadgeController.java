package com.jigubangbang.quest_service.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jigubangbang.quest_service.service.BadgeService;

@RestController
@RequestMapping("/quests")
public class BadgeController {
    @Autowired
    private BadgeService badgeService;

    @GetMapping("/badges")
    public ResponseEntity<Map<String, Object>> getAllBadges(){
        Map<String, Object> result = badgeService.getAllBadges();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/badges/search")
    public ResponseEntity<Map<String, Object>> searchBadges(
        @RequestParam String keyword
    ){
        Map<String, Object> result = badgeService.searchBadges(keyword);
        return ResponseEntity.ok(result);
    }
}
