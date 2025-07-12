package com.jigubangbang.com_service.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jigubangbang.com_service.model.TravelInfoRequestDto;
import com.jigubangbang.com_service.model.TravelInfoResponseDto;

@Mapper
public interface TravelinfoMapper {
    //조회
    List<Map<String, Object>> findTravelInfoList(
        @Param("offset") int offset,
        @Param("limit") int limit,
        @Param("themes") List<Integer> themes,
        @Param("sortOption") String sortOption,
        @Param("search") String search
    );
    long countTravelInfoList(
        @Param("themes") List<Integer> themes,
        @Param("search") String search
    );
    List<Long> findLikedTravelInfoIdsByUserId(@Param("userId") String userId);
    void insertLike(@Param("travelInfoId") Long travelInfoId, @Param("userId") String userId);
    void deleteLike(@Param("travelInfoId") Long travelInfoId, @Param("userId") String userId);
    boolean existsLike(@Param("travelInfoId") Long travelInfoId, @Param("userId") String userId);
    boolean existsTravelInfo(@Param("travelInfoId") Long travelInfoId);

    //참여
    void insertTravelInfoMember(@Param("travelInfoId") Long travelInfoId, @Param("userId") String userId);
    boolean existsTravelInfoMember(@Param("travelInfoId") Long travelInfoId, @Param("userId") String userId);
    List<Long> findJoinedTravelInfoIdsByUserId(@Param("userId") String userId);

    //생성 / 수정
    TravelInfoResponseDto selectTravelInfoById(@Param("id") Long id);
    Long insertTravelInfo(TravelInfoRequestDto requestDTO);
    void updateTravelInfo(TravelInfoRequestDto requestDTO);
    List<Integer> selectThemeIdsByTravelInfoId(@Param("travelInfoId") Long travelInfoId);
    String selectThemeNamesByIds(@Param("themeIds") List<Integer> themeIds);
    void insertTravelInfoThemes(@Param("travelInfoId") Long travelInfoId, @Param("themeIds") List<Integer> themeIds);
    void deleteTravelInfoThemes(@Param("travelInfoId") Long travelInfoId);

    //삭제
    TravelInfoResponseDto selectTravelInfoForDelete(Long travelinfoId);
    int deleteTravelInfoById(Long travelinfoId);
    int deleteChatMessagesByTravelInfoId(Long travelinfoId);
    int deleteChatParticipantsByTravelInfoId(Long travelinfoId);
    int deleteChatRoomByTravelInfoId(Long travelinfoId);
    int deleteLikesByTravelInfoId(Long travelinfoId);

}
