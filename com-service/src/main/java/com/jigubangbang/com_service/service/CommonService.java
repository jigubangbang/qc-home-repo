package com.jigubangbang.com_service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jigubangbang.com_service.model.ChatRoomDto;
import com.jigubangbang.com_service.model.CreateReportRequest;
import com.jigubangbang.com_service.model.MyAppliedTravelmateDto;
import com.jigubangbang.com_service.model.MyCompletedTravelmateDto;
import com.jigubangbang.com_service.model.MyHostedTravelInfoDto;
import com.jigubangbang.com_service.model.MyHostedTravelmateDto;
import com.jigubangbang.com_service.model.MyJoinedTravelInfoDto;
import com.jigubangbang.com_service.model.MyJoinedTravelmateDto;
import com.jigubangbang.com_service.model.MyLikedTravelInfoDto;
import com.jigubangbang.com_service.model.MyLikedTravelmateDto;
import com.jigubangbang.com_service.model.MyTravelerDataDto;
import com.jigubangbang.com_service.model.Report;
import com.jigubangbang.com_service.model.TravelmateApplicationDto;
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

    public boolean existsChatRoom(String groupType, Long groupId) {
        try {
            return commonMapper.existsChatRoom(groupType, groupId);
        } catch (Exception e) {
            System.err.println("채팅방 존재 여부 확인 중 오류 발생 - groupType: " + groupType + ", groupId: " + groupId + ", 오류: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

     public MyTravelerDataDto getMyTravelerData(String currentUserId) {
        MyTravelerDataDto data = new MyTravelerDataDto();

        // 1. 내가 주최한 여행자 동행 모임
        List<MyHostedTravelmateDto> hostedTravelmates = commonMapper.getMyHostedTravelmates(currentUserId);
        
        // 각 주최한 모임의 신청 정보 가져오기
        for (MyHostedTravelmateDto travelmate : hostedTravelmates) {
            List<TravelmateApplicationDto> applications = commonMapper.getTravelmateApplications(travelmate.getId());
            travelmate.setApplications(applications);
        }
        data.setHostedTravelmates(hostedTravelmates);

        // 2. 내가 참여 중인 동행 모임
        List<MyJoinedTravelmateDto> joinedTravelmates = commonMapper.getMyJoinedTravelmates(currentUserId);
        data.setJoinedTravelmates(joinedTravelmates);

        // 3. 내가 신청 중인 동행 모임
        List<MyAppliedTravelmateDto> appliedTravelmates = commonMapper.getMyAppliedTravelmates(currentUserId);
        data.setAppliedTravelmates(appliedTravelmates);

        // 4. 내가 좋아요를 누른 모임
        List<MyLikedTravelmateDto> likedTravelmates = commonMapper.getMyLikedTravelmates(currentUserId);
        data.setLikedTravelmates(likedTravelmates);

        // 5. 내가 참여 완료한 모임
        List<MyCompletedTravelmateDto> completedTravelmates = commonMapper.getMyCompletedTravelmates(currentUserId);
        data.setCompletedTravelmates(completedTravelmates);

        // 6. 내가 참여 중인 정보 공유방
        List<MyJoinedTravelInfoDto> joinedTravelInfos = commonMapper.getMyJoinedTravelInfos(currentUserId);
        // 테마 ID 리스트 설정
        for (MyJoinedTravelInfoDto travelInfo : joinedTravelInfos) {
            List<Integer> themeIds = commonMapper.getTravelInfoThemeIds(travelInfo.getId());
            travelInfo.setThemeIds(themeIds);
        }
        for (MyJoinedTravelInfoDto travelInfo : joinedTravelInfos) {
            List<Integer> themeIds = commonMapper.getTravelInfoThemeIds(travelInfo.getId());
            travelInfo.setThemeIds(themeIds);
            travelInfo.setIsJoined(true); // 참여 중인 정보방은 항상 true
        }
        data.setJoinedTravelInfos(joinedTravelInfos);

        // 7. 내가 좋아요를 누른 정보 공유방
        List<MyLikedTravelInfoDto> likedTravelInfos = commonMapper.getMyLikedTravelInfos(currentUserId);
        // 테마 ID 리스트 설정
        for (MyLikedTravelInfoDto travelInfo : likedTravelInfos) {
            List<Integer> themeIds = commonMapper.getTravelInfoThemeIds(travelInfo.getId());
            travelInfo.setThemeIds(themeIds);
        }
        for (MyLikedTravelInfoDto travelInfo : likedTravelInfos) {
            List<Integer> themeIds = commonMapper.getTravelInfoThemeIds(travelInfo.getId());
            travelInfo.setThemeIds(themeIds);
            // 참가 여부 확인
            boolean isJoined = commonMapper.isUserJoinedTravelInfo(travelInfo.getId(), currentUserId);
            travelInfo.setIsJoined(isJoined);
        }
        data.setLikedTravelInfos(likedTravelInfos);

        // 8. 내가 주최 중인 정보 공유방
        List<MyHostedTravelInfoDto> hostedTravelInfos = commonMapper.getMyHostedTravelInfos(currentUserId);
        // 테마 ID 리스트 설정
        for (MyHostedTravelInfoDto travelInfo : hostedTravelInfos) {
            List<Integer> themeIds = commonMapper.getTravelInfoThemeIds(travelInfo.getId());
            travelInfo.setThemeIds(themeIds);
        }
        for (MyHostedTravelInfoDto travelInfo : hostedTravelInfos) {
            List<Integer> themeIds = commonMapper.getTravelInfoThemeIds(travelInfo.getId());
            travelInfo.setThemeIds(themeIds);
            travelInfo.setIsJoined(true); // 주최한 정보방은 항상 true
        }
        data.setHostedTravelInfos(hostedTravelInfos);

        return data;
    }
}
