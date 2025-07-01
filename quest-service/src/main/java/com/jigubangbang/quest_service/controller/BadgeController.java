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
    public ResponseEntity<Map<String, Object>> getAllBadges(
        @RequestParam(defaultValue="1") int pageNum,
        @RequestParam(required=false) String search,
        @RequestParam(defaultValue = "100") int limit
    ){
        Map<String, Object> result = badgeService.getAllBadges(pageNum, search, limit);
        return ResponseEntity.ok(result);
    }

}
