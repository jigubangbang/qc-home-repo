package com.jigubangbang.com_service.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
}
