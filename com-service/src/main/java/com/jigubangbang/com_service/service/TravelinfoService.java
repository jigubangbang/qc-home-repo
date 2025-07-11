package com.jigubangbang.com_service.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jigubangbang.com_service.model.TravelInfoDto;
import com.jigubangbang.com_service.model.TravelInfoListResponse;
import com.jigubangbang.com_service.repository.TravelinfoMapper;

@Service
public class TravelinfoService {
    @Autowired
    private TravelinfoMapper travelinfoMapper;

    public TravelInfoListResponse getTravelInfoList(
            int pageNum,
            int pageSize,
            int offset,
            List<Integer> themes,
            String sortOption,
            String search) {

        try {
            // 여행정보 목록 조회
            List<Map<String, Object>> travelInfoMapList = travelinfoMapper.findTravelInfoList(
                    offset, pageSize, themes, sortOption, search
            );

            // Map을 DTO로 변환
            List<TravelInfoDto> travelInfos = travelInfoMapList.stream()
                    .map(this::convertMapToDto)
                    .collect(Collectors.toList());

            // 총 개수 조회
            long totalCount = travelinfoMapper.countTravelInfoList(themes, search);

            return new TravelInfoListResponse(travelInfos, (int) totalCount);

        } catch (Exception e) {
            System.err.println("여행정보 목록 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("여행정보 목록을 불러오는데 실패했습니다.", e);
        }
    }

    // 좋아요한 여행정보 목록 조회
    public List<Long> getLikedTravelInfoIds(String userId) {
        try {
            return travelinfoMapper.findLikedTravelInfoIdsByUserId(userId);
        } catch (Exception e) {
            System.err.println("좋아요 목록 조회 중 오류 발생 - 사용자: " + userId + ", 오류: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("좋아요 목록을 불러오는데 실패했습니다.", e);
        }
    }

    // 좋아요 추가
    public void addLike(Long travelInfoId, String userId) {
        try {
            // 여행정보 존재 여부 확인
            if (!travelinfoMapper.existsTravelInfo(travelInfoId)) {
                throw new IllegalArgumentException("존재하지 않는 여행정보입니다.");
            }

            // 이미 좋아요한 여행정보인지 확인
            if (travelinfoMapper.existsLike(travelInfoId, userId)) {
                throw new IllegalArgumentException("이미 좋아요한 여행정보입니다.");
            }

            // 좋아요 추가
            travelinfoMapper.insertLike(travelInfoId, userId);

        } catch (IllegalArgumentException e) {
            System.err.println("좋아요 추가 실패: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("좋아요 추가 중 오류 발생 - 여행정보: " + travelInfoId + ", 사용자: " + userId + ", 오류: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("좋아요 추가에 실패했습니다.", e);
        }
    }

    // 좋아요 제거
    public void removeLike(Long travelInfoId, String userId) {
        try {
            // 좋아요 존재 여부 확인
            if (!travelinfoMapper.existsLike(travelInfoId, userId)) {
                throw new IllegalArgumentException("좋아요하지 않은 여행정보입니다.");
            }

            // 좋아요 제거
            travelinfoMapper.deleteLike(travelInfoId, userId);

        } catch (IllegalArgumentException e) {
            System.err.println("좋아요 제거 실패: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("좋아요 제거 중 오류 발생 - 여행정보: " + travelInfoId + ", 사용자: " + userId + ", 오류: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("좋아요 제거에 실패했습니다.", e);
        }
    }

    /**
     * Map을 TravelInfoDto로 변환
     */
    private TravelInfoDto convertMapToDto(Map<String, Object> map) {
        TravelInfoDto dto = new TravelInfoDto();
        
        try {
            dto.setId(getLongValue(map, "id"));
            dto.setTitle(getStringValue(map, "title"));
            dto.setSimpleDescription(getStringValue(map, "simpleDescription"));
            dto.setCreatorId(getStringValue(map, "creatorId"));
            dto.setCreatorNickname(getStringValue(map, "creatorNickname"));
            dto.setEnterDescription(getStringValue(map, "enterDescription"));
            dto.setThumbnailImage(getStringValue(map, "thumbnailImage"));
            dto.setCreatedAt(getLocalDateTimeValue(map, "createdAt"));
            dto.setBlindStatus(getStringValue(map, "blindStatus"));
            dto.setThemeIds(parseThemeIds(getStringValue(map, "themeIds")));
            dto.setLikeCount(getIntValue(map, "likeCount"));
            dto.setMemberCount(getIntValue(map, "memberCount"));
            dto.setLatestMessage(getStringValue(map, "latestMessage"));
            dto.setChatCount(getIntValue(map, "chatCount"));
        } catch (Exception e) {
            System.err.println("Map을 DTO로 변환 중 오류 발생: " + map + ", 오류: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("데이터 변환에 실패했습니다.", e);
        }
        
        return dto;
    }

    private String getStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }

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

    private LocalDateTime getLocalDateTimeValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return null;
        
        if (value instanceof LocalDateTime) {
            return (LocalDateTime) value;
        } else if (value instanceof java.sql.Timestamp) {
            return ((java.sql.Timestamp) value).toLocalDateTime();
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

    private List<Integer> parseThemeIds(String themeIdsStr) {
        if (themeIdsStr == null || themeIdsStr.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        try {
            return Arrays.stream(themeIdsStr.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            System.err.println("테마 ID 파싱 실패: " + themeIdsStr);
            return new ArrayList<>();
        }
    }

    // 여행정보 참여
    public void joinTravelInfo(Long travelInfoId, String userId) {
        try {
            // 여행정보 존재 여부 확인
            if (!travelinfoMapper.existsTravelInfo(travelInfoId)) {
                throw new IllegalArgumentException("존재하지 않는 여행정보입니다.");
            }

            // 이미 참여했는지 확인
            if (travelinfoMapper.existsTravelInfoMember(travelInfoId, userId)) {
                throw new IllegalArgumentException("이미 참여한 여행정보입니다.");
            }

            // 참여 추가
            travelinfoMapper.insertTravelInfoMember(travelInfoId, userId);

        } catch (IllegalArgumentException e) {
            System.err.println("여행정보 참여 실패: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("여행정보 참여 중 오류 발생 - 여행정보: " + travelInfoId + ", 사용자: " + userId + ", 오류: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("여행정보 참여에 실패했습니다.", e);
        }
    }

    // 참여한 여행정보 목록 조회
    public List<Long> getJoinedTravelInfoIds(String userId) {
        try {
            return travelinfoMapper.findJoinedTravelInfoIdsByUserId(userId);
        } catch (Exception e) {
            System.err.println("참여한 여행정보 목록 조회 중 오류 발생 - 사용자: " + userId + ", 오류: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("참여한 여행정보 목록을 불러오는데 실패했습니다.", e);
        }
    }
}
