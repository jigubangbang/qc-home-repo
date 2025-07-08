package com.jigubangbang.quest_service.chat_service;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.jigubangbang.quest_service.model.chat_service.BadgeNotificationRequestDto;

@FeignClient( name="chat-service", configuration = NotificationServiceClientConfig.class, fallback = NotificationServiceClientFallback.class)
public interface NotificationServiceClient {

    // 뱃지 획득 알림
    @PostMapping("/quests/badges/earned")
    public ResponseEntity<Map<String, Object>> createBadgeEarnedNotification(@RequestBody BadgeNotificationRequestDto request);
    
    // 뱃지 수거 알림 (관리자가 점검 후 뱃지 취소)
    @PostMapping("/admin/badges/revoked")
    public ResponseEntity<Map<String, Object>> createBadgeRevokedNotification(@RequestBody BadgeNotificationRequestDto request);
}
