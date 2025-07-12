package com.jigubangbang.com_service.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jigubangbang.com_service.model.ChatRoomDto;
import com.jigubangbang.com_service.model.CreateReportRequest;
import com.jigubangbang.com_service.model.Report;
import com.jigubangbang.com_service.repository.CommonMapper;

@Service
public class CommonService {
    @Autowired
    private CommonMapper commonMapper;

    public void createReport(CreateReportRequest request, String reporterId) {
        // 자기 자신 신고 방지
        if (reporterId.equals(request.getTargetUserId())) {
            throw new IllegalArgumentException("자기 자신을 신고할 수 없습니다.");
        }
        
        // 중복 신고 체크
        boolean isDuplicate = commonMapper.existsByReporterAndContent(
            reporterId, 
            request.getContentSubtype(), 
            request.getContentId()
        );
        
        if (isDuplicate) {
            throw new IllegalArgumentException("이미 신고한 컨텐츠입니다.");
        }
        
        // 신고 생성
        Report report = Report.builder()
            .reporterId(reporterId)
            .targetUserId(request.getTargetUserId())
            .contentSubtype(request.getContentSubtype())
            .contentType(request.getContentType())
            .contentId(request.getContentId())
            .reasonCode(request.getReasonCode())
            .reasonText(request.getReasonText())
            .reportStatus("PENDING")
            .build();
            
        commonMapper.insertReport(report);
    }

    public String getUserProfile(String userId){
        return commonMapper.getUserProfile(userId);
    }

    public ChatRoomDto getChatRoom(String groupType, Long groupId) {
        try {
            // 기존 채팅방 조회
            ChatRoomDto chatRoom = commonMapper.findChatRoomByGroup(groupType, groupId);
            
            if (chatRoom == null) {
                // 채팅방이 없으면 새로 생성
                commonMapper.insertChatRoom(groupType, groupId);
                
                // 생성된 채팅방 다시 조회
                chatRoom = commonMapper.findChatRoomByGroup(groupType, groupId);
                
                if (chatRoom == null) {
                    throw new RuntimeException("채팅방 생성에 실패했습니다.");
                }
            }
            
            return chatRoom;
            
        } catch (Exception e) {
            System.err.println("채팅방 조회/생성 중 오류 발생 - groupType: " + groupType + ", groupId: " + groupId + ", 오류: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("채팅방을 불러오는데 실패했습니다.", e);
        }
    }

    /**
     * 채팅방 존재 여부 확인
     */
    public boolean existsChatRoom(String groupType, Long groupId) {
        try {
            return commonMapper.existsChatRoom(groupType, groupId);
        } catch (Exception e) {
            System.err.println("채팅방 존재 여부 확인 중 오류 발생 - groupType: " + groupType + ", groupId: " + groupId + ", 오류: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
