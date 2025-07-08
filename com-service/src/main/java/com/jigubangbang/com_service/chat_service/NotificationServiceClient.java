package com.jigubangbang.com_service.chat_service;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.jigubangbang.com_service.model.chat_service.ComNotificationRequestDto;
import com.jigubangbang.com_service.model.chat_service.GroupAcceptedNotificationRequestDto;
import com.jigubangbang.com_service.model.chat_service.GroupApplyNotificationRequestDto;

@FeignClient( name="chat-service", configuration = NotificationServiceClientConfig.class, fallback = NotificationServiceClientFallback.class)
public interface NotificationServiceClient {

    // 게시물 댓글 알림
    @PostMapping("/posts/comment")
    ResponseEntity<Map<String, Object>> createPostCommentNotification(@RequestBody ComNotificationRequestDto request);
    
    // 투표기한 만료 알림(선택사항)

    // 그룹 가입 신청 알림
    @PostMapping("/travelgroup/applications")
    public ResponseEntity<Map<String, Object>> createGroupApplyNotification(@RequestBody GroupApplyNotificationRequestDto request);

    // 그룹 가입 알림
    @PostMapping("/travelgroup/applications/{applicantId}")
    public ResponseEntity<Map<String, Object>> createGroupAcceptedNotification(@RequestBody GroupAcceptedNotificationRequestDto request);
}
