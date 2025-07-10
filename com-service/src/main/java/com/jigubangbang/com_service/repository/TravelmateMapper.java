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
import com.jigubangbang.com_service.model.TravelmateListResponse;

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
}
