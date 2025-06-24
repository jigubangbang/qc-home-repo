package com.jigubangbang.quest_service.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jigubangbang.quest_service.model.QuestCerti;
import com.jigubangbang.quest_service.model.QuestUserDto;
import com.jigubangbang.quest_service.model.UserJourneyDto;
import com.jigubangbang.quest_service.service.UserQuestService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user-quests")
public class UserQuestController {
    @Autowired
    private UserQuestService userQuestService;

    //퀘스트 도전
    @PostMapping("/challenge")
    public ResponseEntity<Map<String, Object>> challengeQuest(
        HttpServletRequest request,
        @RequestParam("quest_id") int quest_id
    ){
        //#NeedToChange
        //session에서 user id 받아오기
        String user_id = "aaa";
        Map<String, Object> response = new HashMap<>();

        //중복 체크
        if (userQuestService.countUserQuest(user_id, quest_id)>0){
            response.put("success", false);
            response.put("message", "이미 도전 중인 퀘스트입니다");
            return ResponseEntity.ok(response);
        }

        userQuestService.challengeQuest(user_id, quest_id);
        response.put("success", true);
        response.put("message", "퀘스트 도전 완료");
        return ResponseEntity.ok(response);
    }

    //My Quest Journey stats id, nickname, profile, 퀘스트 완료 수, 레벨
    //#NeedToChange 
    @GetMapping("/journey")
    public ResponseEntity<UserJourneyDto> getUserJourney(HttpServletRequest request){
        //#NeedToChange
        String user_id = "aaa";
        UserJourneyDto journey = userQuestService.getUserJourney(user_id);
        return ResponseEntity.ok(journey);
    }

    //퀘스트 목록 조회
    @GetMapping("/detail")
    public ResponseEntity<List<QuestUserDto>> getUserQuestList(HttpServletRequest  request,
        @RequestParam(required=false) String user_id,
        @RequestParam(required=false) String order,
        @RequestParam(required=false) String status
    ){
        //#NeedToChange 
        //user_id가 null 값이라면
        if (user_id == null || user_id.isEmpty()){
            //현재 로그인한 유저의 아이디
            user_id = "aaa";
        }

        String orderColumn = null;
        if ("xp".equals(order)) {
            orderColumn = "xp";
        } else if ("started_at".equals(order)) {
            orderColumn = "started_at";
        } else if ("completed_at".equals(order)) {
            orderColumn = "completed_at";
        }

        String statusFilter = null;
        if ("IN_PROGRESS".equals(status) || "COMPLETED".equals(status) || "GIVEN_UP".equals(status)) {
            statusFilter = status;
        }

        List<QuestUserDto> questList = userQuestService.getUserQuestList(user_id, orderColumn, statusFilter);
        return ResponseEntity.ok(questList);
    }

    //퀘스트 인증 조회
    @GetMapping("/{quest_user_id}")
    public ResponseEntity<QuestCerti> getQuestCerti(
        @PathVariable("quest_user_id") int quest_user_id
    ){
        QuestCerti questCerti = userQuestService.getQuestCerti(quest_user_id);
        if(questCerti == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(questCerti);
    }
    

    //퀘스트 완료 제출
    @PostMapping("/{quest_user_id}/complete")
    public ResponseEntity<Map<String, Object>> completeQuest(
        @PathVariable("quest_user_id") int quest_user_id,
        @RequestBody QuestCerti completeRequest)
    {
        Map<String, Object> response = new HashMap<>();
        userQuestService.completeQuest(quest_user_id, completeRequest);
        response.put("success", true);
        response.put("message", "퀘스트 인증 전송 완료");
        return ResponseEntity.ok(response);    
    }


    //퀘스트 포기
    @PostMapping("/{quest_user_id}/abandon")
    public ResponseEntity<Map<String, Object>> abandonQuest(
        @PathVariable("quest_user_id") int quest_user_id)
    {
        Map<String, Object> response = new HashMap<>();

        userQuestService.abandonQuest(quest_user_id);
        response.put("success", true);
        response.put("message", "퀘스트를 포기했습니다.");
        return ResponseEntity.ok(response);
    }

}
