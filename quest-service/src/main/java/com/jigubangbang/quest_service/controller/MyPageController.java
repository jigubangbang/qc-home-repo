package com.jigubangbang.quest_service.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jigubangbang.quest_service.service.QuestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/quests")
public class MyPageController {
    @Autowired
    private QuestService questService;

    @GetMapping("/my-page")
    public ResponseEntity<Map<String, Object>> getUserPage(
        @RequestParam(required=false) String user_id
    ) {
        if (user_id == null || user_id.isEmpty()){
            //#NeedToChange
            //현재 로그인한 유저의 아이디
            user_id = "aaa";
        }

        try {
            Map<String, Object> result = questService.getUserPageData(user_id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "사용자 페이지 데이터를 불러오는데 실패했습니다."));
        }
    }
    
}
