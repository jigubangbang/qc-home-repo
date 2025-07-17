package com.jigubangbang.quest_service.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jigubangbang.quest_service.model.QuestCerti;
import com.jigubangbang.quest_service.model.SimpleUserDto;
import com.jigubangbang.quest_service.model.UserLevelInfoDto;
import com.jigubangbang.quest_service.service.QuestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/quests")
public class MyPageController {
    @Autowired
    private QuestService questService;

    @GetMapping("/user-level/{userId}")
    public ResponseEntity<UserLevelInfoDto> getUserLevelInfo(@PathVariable String userId) {
        try {
            UserLevelInfoDto levelInfo = questService.getUserLevelInfo(userId);
            if (levelInfo == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(levelInfo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/my-page")
    public ResponseEntity<Map<String, Object>> getUserPage(
            @RequestParam(required = false) String user_id,
            @RequestHeader(value = "User-Id", required = false) String headerUserId) {
        try {
            // user_id가 null이면 헤더에서 받아온 userId 사용
            String targetUserId = (user_id != null) ? user_id : headerUserId;
            
            Map<String, Object> result = questService.getUserPageData(targetUserId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "사용자 페이지 데이터를 불러오는데 실패했습니다."));
        }
    }

    @GetMapping("/certificate/{quest_user_id}")
    public ResponseEntity<QuestCerti> getQuestCerti(
        @PathVariable("quest_user_id") int quest_user_id
    ){
        QuestCerti questCerti = questService.getQuestCerti(quest_user_id);
        if(questCerti == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(questCerti);
    }

    @GetMapping("/user-graph/{user_id}")
    public ResponseEntity<Map<String, Object>> getUserGraph(
        @PathVariable("user_id") String user_id
    ){
        List<SimpleUserDto> userData = questService.getUserGraph();
        int rank = questService.getUserRank(user_id);
        int countUser = questService.getCountUser();

        Map<String, Object> response = new HashMap<>();
        response.put("userData", userData);

        response.put("rank", rank);
        response.put("countUser", countUser);
        response.put("user", user_id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user-graph")
    public ResponseEntity<Map<String, Object>> getMyUserGraph(
        @RequestParam("user_id") String userId
    ){
        List<SimpleUserDto> userData = questService.getUserGraph();
        int rank = questService.getUserRank(userId);
        int countUser = questService.getCountUser();

        Map<String, Object> response = new HashMap<>();
        response.put("userData", userData);
        response.put("rank", rank);
        response.put("countUser", countUser);
        response.put("user", userId);

        return ResponseEntity.ok(response);
    }

    
}
