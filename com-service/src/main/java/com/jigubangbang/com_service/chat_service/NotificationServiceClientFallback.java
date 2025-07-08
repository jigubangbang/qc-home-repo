package com.jigubangbang.com_service.chat_service;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.jigubangbang.com_service.model.chat_service.ComNotificationRequestDto;
import com.jigubangbang.com_service.model.chat_service.GroupAcceptedNotificationRequestDto;
import com.jigubangbang.com_service.model.chat_service.GroupApplyNotificationRequestDto;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class NotificationServiceClientFallback implements NotificationServiceClient {
    
    @Override
    public ResponseEntity<Map<String, Object>> createPostCommentNotification(ComNotificationRequestDto request) {
        log.warn("[Fallback] 댓글 알림 전송 실패 - 메인 기능은 정상 처리됨");
        return ResponseEntity.ok(Map.of("success", false, "fallback", true));
    }
    
    @Override
    public ResponseEntity<Map<String, Object>> createGroupApplyNotification(GroupApplyNotificationRequestDto request) {
        log.warn("[Fallback] 그룹 신청 알림 전송 실패 - 메인 기능은 정상 처리됨");
        return ResponseEntity.ok(Map.of("success", false, "fallback", true));
    }
    
    @Override
    public ResponseEntity<Map<String, Object>> createGroupAcceptedNotification(GroupAcceptedNotificationRequestDto request) {
        log.warn("[Fallback] 그룹 수락 알림 전송 실패 - 메인 기능은 정상 처리됨");
        return ResponseEntity.ok(Map.of("success", false, "fallback", true));
    }
}
