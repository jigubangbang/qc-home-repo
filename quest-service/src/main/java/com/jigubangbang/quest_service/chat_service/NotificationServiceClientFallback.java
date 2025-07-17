package com.jigubangbang.quest_service.chat_service;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.jigubangbang.quest_service.model.chat_service.BadgeNotificationRequestDto;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class NotificationServiceClientFallback implements NotificationServiceClient {
    
    @Override
    public ResponseEntity<Map<String, Object>> createBadgeEarnedNotification(@RequestBody BadgeNotificationRequestDto request) {
        log.warn("[Fallback] 뱃지 획득 알림 전송 실패 - 메인 기능은 정상 처리됨");
        return ResponseEntity.ok(Map.of("success", false, "fallback", true));
    }
    
    @Override
    public ResponseEntity<Map<String, Object>> createBadgeRevokedNotification(@RequestBody BadgeNotificationRequestDto request) {
        log.warn("[Fallback] 뱃지 수거 알림 전송 실패 - 메인 기능은 정상 처리됨");
        return ResponseEntity.ok(Map.of("success", false, "fallback", true));
    }

}
