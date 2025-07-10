package com.jigubangbang.com_service.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jigubangbang.com_service.model.CityDto;
import com.jigubangbang.com_service.model.CountryDto;
import com.jigubangbang.com_service.model.TargetDto;
import com.jigubangbang.com_service.model.ThemeDto;
import com.jigubangbang.com_service.model.TravelStyleDto;
import com.jigubangbang.com_service.model.TravelmateDto;
import com.jigubangbang.com_service.model.TravelmateListResponse;
import com.jigubangbang.com_service.repository.TravelmateMapper;

@Service
public class TravelmateService {
    @Autowired
    private TravelmateMapper travelmateMapper;

    //국가 목록 조회
    public List<CountryDto> getAllCountries() {
        return travelmateMapper.selectAllCountries();
    }

    //도시 목록 조회
    public List<CityDto> getCitiesByCountryId(String countryId) {
        return travelmateMapper.selectCitiesByCountryId(countryId);
    }

    //필터
    public List<TargetDto> getAllTargets() {
        return travelmateMapper.selectAllTargets();
    }

    public List<ThemeDto> getAllThemes() {
        return travelmateMapper.selectAllThemes();
    }

    public List<TravelStyleDto> getAllTravelStyles() {
        return travelmateMapper.selectAllTravelStyles();
    }

    //리스트
    public TravelmateListResponse getTravelmateList(
            int pageNum,
            int pageSize,
            int offset,
            List<String> locations,
            List<Integer> targets,
            List<Integer> themes,
            List<String> styles,
            List<String> continents,
            String sortOption,
            boolean showCompleted,
            String search,
            String startDate,
            String endDate) {

        try {
            // 여행메이트 목록 조회
            List<Map<String, Object>> travelmateMapList = travelmateMapper.findTravelmateList(
                    offset, pageSize, locations, targets, themes, styles, continents, sortOption, showCompleted, search, startDate, endDate
            );

            // Map을 DTO로 변환
            List<TravelmateDto> travelmates = travelmateMapList.stream()
                    .map(this::convertMapToDto)
                    .collect(Collectors.toList());

            // 총 개수 조회
            long totalCount = travelmateMapper.countTravelmateList(
                    locations, targets, themes, styles, continents, showCompleted, search, startDate, endDate
            );

            return new TravelmateListResponse(travelmates, (int) totalCount);

        } catch (Exception e) {
            System.err.println("여행메이트 목록 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("여행메이트 목록을 불러오는데 실패했습니다.", e);
        }
    }

    // 좋아요한 게시글 목록 조회
    public List<Long> getLikedPostIds(String userId) {
        try {
            return travelmateMapper.findLikedPostIdsByUserId(userId);
        } catch (Exception e) {
            System.err.println("좋아요 목록 조회 중 오류 발생 - 사용자: " + userId + ", 오류: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("좋아요 목록을 불러오는데 실패했습니다.", e);
        }
    }

    // 좋아요 추가
    public void addLike(Long postId, String userId) {
        try {
            // 게시글 존재 여부 확인
            if (!travelmateMapper.existsPost(postId)) {
                throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
            }

            // 이미 좋아요한 게시글인지 확인
            if (travelmateMapper.existsLike(postId, userId)) {
                throw new IllegalArgumentException("이미 좋아요한 게시글입니다.");
            }

            // 좋아요 추가
            travelmateMapper.insertLike(postId, userId);

        } catch (IllegalArgumentException e) {
            System.err.println("좋아요 추가 실패: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("좋아요 추가 중 오류 발생 - 게시글: " + postId + ", 사용자: " + userId + ", 오류: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("좋아요 추가에 실패했습니다.", e);
        }
    }

    // 좋아요 제거
    public void removeLike(Long postId, String userId) {
        try {
            // 좋아요 존재 여부 확인
            if (!travelmateMapper.existsLike(postId, userId)) {
                throw new IllegalArgumentException("좋아요하지 않은 게시글입니다.");
            }

            // 좋아요 제거
            travelmateMapper.deleteLike(postId, userId);

        } catch (IllegalArgumentException e) {
            System.err.println("좋아요 제거 실패: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("좋아요 제거 중 오류 발생 - 게시글: " + postId + ", 사용자: " + userId + ", 오류: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("좋아요 제거에 실패했습니다.", e);
        }
    }

    /**
     * Map을 TravelmateDto로 변환
     */
    private TravelmateDto convertMapToDto(Map<String, Object> map) {
        TravelmateDto dto = new TravelmateDto();
        
        try {
            dto.setId(getLongValue(map, "id"));
            dto.setTitle(getStringValue(map, "title"));
            dto.setSimpleDescription(getStringValue(map, "simpleDescription"));
            dto.setThumbnailImage(getStringValue(map, "thumbnailImage"));
            dto.setCreatorNickname(getStringValue(map, "creatorNickname"));
            dto.setCreatorStyle(getStringValue(map, "creatorStyle"));
            dto.setStartAt(getLocalDateTimeValue(map, "startAt"));
            dto.setEndAt(getLocalDateTimeValue(map, "endAt"));
            dto.setLocationNames(getStringValue(map, "locationNames"));
            dto.setThemeNames(getStringValue(map, "themeNames"));
            dto.setStyleNames(getStringValue(map, "styleNames"));
            dto.setTargetNames(getStringValue(map, "targetNames"));
            dto.setLikeCount(getIntValue(map, "likeCount"));
            dto.setViewCount(getIntValue(map, "viewCount"));
            dto.setStatus(getStringValue(map, "status"));
            dto.setBlindStatus(getStringValue(map, "blindStatus"));
        } catch (Exception e) {
            System.err.println("Map을 DTO로 변환 중 오류 발생: " + map + ", 오류: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("데이터 변환에 실패했습니다.", e);
        }
        
        return dto;
    }

    /**
     * Map에서 String 값 추출
     */
    private String getStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }

    /**
     * Map에서 Long 값 추출
     */
    private Long getLongValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return null;
        
        if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof Integer) {
            return ((Integer) value).longValue();
        } else if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                System.err.println("Long 변환 실패 - key: " + key + ", value: " + value);
                return null;
            }
        }
        return null;
    }

    /**
     * Map에서 Integer 값 추출
     */
    private Integer getIntValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return 0;
        
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Long) {
            return ((Long) value).intValue();
        } else if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                System.err.println("Integer 변환 실패 - key: " + key + ", value: " + value);
                return 0;
            }
        }
        return 0;
    }

    /**
     * Map에서 LocalDateTime 값 추출
     */
    private LocalDateTime getLocalDateTimeValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return null;
        
        if (value instanceof LocalDateTime) {
            return (LocalDateTime) value;
        } else if (value instanceof Timestamp) {
            return ((Timestamp) value).toLocalDateTime();
        } else if (value instanceof java.sql.Date) {
            return ((java.sql.Date) value).toLocalDate().atStartOfDay();
        } else if (value instanceof java.util.Date) {
            return LocalDateTime.ofInstant(((java.util.Date) value).toInstant(), 
                    java.time.ZoneId.systemDefault());
        }
        
        System.err.println("LocalDateTime 변환 실패 - key: " + key + ", value: " + value + 
                ", type: " + value.getClass().getSimpleName());
        return null;
    }
}
