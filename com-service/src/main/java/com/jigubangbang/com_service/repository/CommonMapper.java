package com.jigubangbang.com_service.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jigubangbang.com_service.model.ChatRoomDto;
import com.jigubangbang.com_service.model.MyAppliedTravelmateDto;
import com.jigubangbang.com_service.model.MyCompletedTravelmateDto;
import com.jigubangbang.com_service.model.MyHostedTravelInfoDto;
import com.jigubangbang.com_service.model.MyHostedTravelmateDto;
import com.jigubangbang.com_service.model.MyJoinedTravelInfoDto;
import com.jigubangbang.com_service.model.MyJoinedTravelmateDto;
import com.jigubangbang.com_service.model.MyLikedTravelInfoDto;
import com.jigubangbang.com_service.model.MyLikedTravelmateDto;
import com.jigubangbang.com_service.model.Report;
import com.jigubangbang.com_service.model.TravelmateApplicationDto;

@Mapper
public interface CommonMapper {

    void insertReport(Report report);
    
    boolean existsByReporterAndContent(
        @Param("reporterId") String reporterId,
        @Param("contentSubtype") String contentSubtype,
        @Param("contentId") Long contentId
    );

    String getUserProfile(String userId);
    ChatRoomDto findChatRoomByGroup(@Param("groupType") String groupType, @Param("groupId") Long groupId);
    void insertChatRoom(@Param("groupType") String groupType, @Param("groupId") Long groupId);
    boolean existsChatRoom(@Param("groupType") String groupType, @Param("groupId") Long groupId);

    //my page
    List<MyHostedTravelmateDto> getMyHostedTravelmates(@Param("currentUserId") String currentUserId);
    
    // 특정 동행 모임의 신청 정보들
    List<TravelmateApplicationDto> getTravelmateApplications(@Param("mateId") Integer mateId);
    
    // 내가 참여 중인 동행 모임
    List<MyJoinedTravelmateDto> getMyJoinedTravelmates(@Param("currentUserId") String currentUserId);
    
    // 내가 신청 중인 동행 모임
    List<MyAppliedTravelmateDto> getMyAppliedTravelmates(@Param("currentUserId") String currentUserId);
    
    // 내가 좋아요를 누른 모임
    List<MyLikedTravelmateDto> getMyLikedTravelmates(@Param("currentUserId") String currentUserId);
    
    // 내가 참여 완료한 모임
    List<MyCompletedTravelmateDto> getMyCompletedTravelmates(@Param("currentUserId") String currentUserId);
    
    // 내가 참여 중인 정보 공유방
    List<MyJoinedTravelInfoDto> getMyJoinedTravelInfos(@Param("currentUserId") String currentUserId);
    
    // 내가 좋아요를 누른 정보 공유방
    List<MyLikedTravelInfoDto> getMyLikedTravelInfos(@Param("currentUserId") String currentUserId);
    
    // 내가 주최 중인 정보 공유방
    List<MyHostedTravelInfoDto> getMyHostedTravelInfos(@Param("currentUserId") String currentUserId);
    
    // 정보 공유방의 테마 ID 리스트 조회
    List<Integer> getTravelInfoThemeIds(@Param("travelInfoId") Integer travelInfoId);
}
