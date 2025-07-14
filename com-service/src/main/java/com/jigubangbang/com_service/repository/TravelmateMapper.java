package com.jigubangbang.com_service.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jigubangbang.com_service.model.CityDto;
import com.jigubangbang.com_service.model.CountryDto;
import com.jigubangbang.com_service.model.TargetDto;
import com.jigubangbang.com_service.model.ThemeDto;
import com.jigubangbang.com_service.model.TravelStyleDto;
import com.jigubangbang.com_service.model.Travelmate;
import com.jigubangbang.com_service.model.TravelmateApplicationDto;
import com.jigubangbang.com_service.model.TravelmateCommentDto;
import com.jigubangbang.com_service.model.TravelmateCreateDto;
import com.jigubangbang.com_service.model.TravelmateListResponse;
import com.jigubangbang.com_service.model.TravelmateMemberDto;
import com.jigubangbang.com_service.model.TravelmatePostDto;
import com.jigubangbang.com_service.model.TravelmateResponseDto;
import com.jigubangbang.com_service.model.TravelmateUpdateDto;

@Mapper
public interface TravelmateMapper {
    //검색
    List<CountryDto> selectAllCountries();
    List<CityDto> selectCitiesByCountryId(String countryId);

    // 필터
    List<TargetDto> selectAllTargets();
    List<ThemeDto> selectAllThemes();
    List<TravelStyleDto> selectAllTravelStyles();

    //리스트
    List<Map<String, Object>> findTravelmateList(
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("locations") List<String> locations,
            @Param("targets") List<Integer> targets,
            @Param("themes") List<Integer> themes,
            @Param("styles") List<String> styles,
            @Param("continents") List<String> continents,
            @Param("sortOption") String sortOption,
            @Param("showCompleted") boolean showCompleted,
            @Param("search") String search,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );

    long countTravelmateList(
            @Param("locations") List<String> locations,
            @Param("targets") List<Integer> targets,
            @Param("themes") List<Integer> themes,
            @Param("styles") List<String> styles,
            @Param("continents") List<String> continents,
            @Param("showCompleted") boolean showCompleted,
            @Param("search") String search,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );

    List<Long> findLikedPostIdsByUserId(@Param("userId") String userId);
    void insertLike(@Param("postId") Long postId, @Param("userId") String userId);
    void deleteLike(@Param("postId") Long postId, @Param("userId") String userId);
    boolean existsLike(@Param("postId") Long postId, @Param("userId") String userId);
    boolean existsPost(@Param("postId") Long postId);

    //상세 조회
        Map<String, Object> findTravelmateDetail(@Param("postId") Long postId);
        String findMemberApplicationStatus(@Param("postId") Long postId, @Param("userId") String userId);
        String findPostStatus(@Param("postId") Long postId);
        String findPostCreatorId(@Param("postId") Long postId);
        void insertMemberApplication(@Param("postId") Long postId, 
                           @Param("userId") String userId, 
                           @Param("description") String description);
        void deleteMemberApplication(@Param("postId") Long postId, @Param("userId") String userId);
        List<TravelmateMemberDto> getTravelmateMembers(@Param("postId") Long postId);
        List<TravelmateCommentDto> getTravelmateComments(@Param("postId") Long postId);

        //댓글들
        void insertComment(@Param("userId") String userId,
                  @Param("mateId") Long mateId,
                  @Param("content") String content,
                  @Param("level") Integer level,
                  @Param("parentId") Long parentId);
        boolean existsComment(@Param("commentId") Long commentId, 
                        @Param("mateId") Long mateId);
        Integer getCommentLevel(@Param("commentId") Long commentId);
        TravelmateCommentDto getCommentById(@Param("commentId") Long commentId);
        void updateComment(@Param("commentId") Long commentId, 
                        @Param("content") String content);
        void softDeleteComment(@Param("commentId") Long commentId);
        void softDeleteCommentWithReplies(@Param("commentId") Long commentId);
        int getReplyCount(@Param("parentId") Long parentId);

        //수정
        Travelmate findById(@Param("id") Long id);
    
    // 모임 기본 정보 업데이트
    void updateTravelmate(TravelmateUpdateDto updateDto);
    
    // 연관 데이터 삭제
    void deleteTravelmateLocations(@Param("travelmateId") Long travelmateId);
    void deleteTravelmateThemes(@Param("travelmateId") Long travelmateId);
    void deleteTravelmateStyles(@Param("travelmateId") Long travelmateId);
    
    // 연관 데이터 등록
    void insertTravelmateLocation(@Param("travelmateId") Long travelmateId, @Param("countryId") String countryId, @Param("cityId") Integer cityId );
    void insertTravelmateTheme(@Param("travelmateId") Long travelmateId, @Param("themeId") Long themeId);
    void insertTravelmateStyle(@Param("travelmateId") Long travelmateId, @Param("styleId") Character styleId);

        void insertTravelmate(TravelmateCreateDto createDto);

        //멤버 등록
        void insertTravelmateMember(@Param("travelmateId") Long travelmateId, 
                               @Param("userId") String userId, 
                               @Param("isCreator") boolean isCreator);

    //삭제
    TravelmateResponseDto selectTravelmateForDelete(Long travelmateId);
    int deleteTravelmateById(Long travelmateId);
    int deleteChatMessagesByTravelmateId(Long travelmateId);
    int deleteGroupUsersByTravelmateId(Long travelmateId);
    int deleteChatRoomByTravelmateId(Long travelmateId);
    int deleteTravelmateChildComments(Long travelmateId);
    int deleteTravelmateParentComments(Long travelmateId);
    int deleteTravelmateLikes(Long travelmateId);
    int deleteTravelmateApplications(Long travelmateId);
    int deleteTravelmateRegions(Long travelmateId);

    //수락/거절
    TravelmateApplicationDto getApplicationById(@Param("applicationId") Integer applicationId);
    void updateApplicationStatus(@Param("applicationId") Integer applicationId, 
                            @Param("status") String status, 
                            @Param("responderId") String responderId);

    TravelmatePostDto getTravelmateById(@Param("travelmateId") Long travelmateId);
    void deleteApplicationById(@Param("applicationId") Integer applicationId);

    // 그룹 멤버 추가
    void addGroupMember(@Param("userId") String userId, 
                    @Param("groupType") String groupType, 
                    @Param("groupId") Long groupId);

    //댓글
    String findPostTitle(Long postId);
}
