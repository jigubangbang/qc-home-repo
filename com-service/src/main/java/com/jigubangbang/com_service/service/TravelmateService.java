package com.jigubangbang.com_service.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jigubangbang.com_service.model.CityDto;
import com.jigubangbang.com_service.model.CountryDto;
import com.jigubangbang.com_service.model.TargetDto;
import com.jigubangbang.com_service.model.ThemeDto;
import com.jigubangbang.com_service.model.TravelStyleDto;
import com.jigubangbang.com_service.model.Travelmate;
import com.jigubangbang.com_service.model.TravelmateApplicationDto;
import com.jigubangbang.com_service.model.TravelmateCommentDto;
import com.jigubangbang.com_service.model.TravelmateCreateDto;
import com.jigubangbang.com_service.model.TravelmateCreateRequest;
import com.jigubangbang.com_service.model.TravelmateDetailDto;
import com.jigubangbang.com_service.model.TravelmateDetailResponse;
import com.jigubangbang.com_service.model.TravelmateDto;
import com.jigubangbang.com_service.model.TravelmateListResponse;
import com.jigubangbang.com_service.model.TravelmateMemberDto;
import com.jigubangbang.com_service.model.TravelmatePostDto;
import com.jigubangbang.com_service.model.TravelmateResponseDto;
import com.jigubangbang.com_service.model.TravelmateUpdateDto;
import com.jigubangbang.com_service.model.TravelmateUpdateRequest;
import com.jigubangbang.com_service.repository.CommonMapper;
import com.jigubangbang.com_service.repository.TravelmateMapper;

@Service
public class TravelmateService {
    @Autowired
    private TravelmateMapper travelmateMapper;

    @Autowired
    private CommonMapper commonMapper;

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
            dto.setMemberCount(getIntValue(map, "memberCount"));
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

    public TravelmateDetailResponse getTravelmateDetail(Long postId) {
        try {
            Map<String, Object> detailMap = travelmateMapper.findTravelmateDetail(postId);
            
            if (detailMap == null) {
                throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
            }
            
            TravelmateDetailDto detail = convertDetailMapToDto(detailMap);
            return new TravelmateDetailResponse(detail);
            
        } catch (Exception e) {
            System.err.println("여행메이트 상세 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("여행메이트 상세 정보를 불러오는데 실패했습니다.", e);
        }
    }

    private TravelmateDetailDto convertDetailMapToDto(Map<String, Object> map) {
        TravelmateDetailDto dto = new TravelmateDetailDto();
        
        try {
            // 기존 필드들
            dto.setId(getLongValue(map, "id"));
            dto.setTitle(getStringValue(map, "title"));
            dto.setSimpleDescription(getStringValue(map, "simpleDescription"));
            dto.setBackgroundImage(getStringValue(map, "backgroundImage"));
            dto.setDescription(getStringValue(map, "description")); 
            dto.setApplicationDescription(getStringValue(map, "applicationDescription")); 
            dto.setThumbnailImage(getStringValue(map, "thumbnailImage"));
            dto.setCreatorNickname(getStringValue(map, "creatorNickname"));
            dto.setCreatorId(getStringValue(map, "creatorId")); 
            dto.setCreatorProfileImage(getStringValue(map, "creatorProfileImage")); // 추가
            dto.setCreatorStyle(getStringValue(map, "creatorStyle"));
            dto.setStartAt(getLocalDateTimeValue(map, "startAt"));
            dto.setEndAt(getLocalDateTimeValue(map, "endAt"));
            dto.setLocationNames(getStringValue(map, "locationNames"));
            dto.setThemeNames(getStringValue(map, "themeNames"));
            dto.setStyleNames(getStringValue(map, "styleNames"));
            dto.setTargetNames(getStringValue(map, "targetNames"));
            dto.setLikeCount(getIntValue(map, "likeCount"));
            dto.setMemberCount(getIntValue(map, "memberCount"));
            dto.setViewCount(getIntValue(map, "viewCount"));
            dto.setStatus(getStringValue(map, "status"));
            dto.setBlindStatus(getStringValue(map, "blindStatus"));
            
        } catch (Exception e) {
            System.err.println("Detail Map을 DTO로 변환 중 오류 발생: " + map + ", 오류: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("상세 데이터 변환에 실패했습니다.", e);
        }
        
        return dto;
    }

    public String getMemberStatus(Long postId, String userId) {
        try {
            // 게시글 존재 여부 확인
            if (!travelmateMapper.existsPost(postId)) {
                throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
            }

            // 멤버 신청 상태 조회
            String applicationStatus = travelmateMapper.findMemberApplicationStatus(postId, userId);
            
            if (applicationStatus == null) {
                return "NOT_MEMBER";
            }
            
            switch (applicationStatus) {
                case "PENDING":
                    return "PENDING";
                case "ACCEPTED":
                    return "MEMBER";
                case "REJECTED":
                    return "NOT_MEMBER";
                default:
                    return "NOT_MEMBER";
            }
            
        } catch (Exception e) {
            System.err.println("멤버 상태 조회 중 오류 발생 - 게시글: " + postId + ", 사용자: " + userId + ", 오류: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("멤버 상태 조회에 실패했습니다.", e);
        }
    }

    public Map<String, Object> joinTravelmate(Long postId, String userId, String description) {
        try {
            // 게시글 존재 여부 확인
            if (!travelmateMapper.existsPost(postId)) {
                throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
            }

            // 게시글 상태 확인 (ACTIVE인지)
            String postStatus = travelmateMapper.findPostStatus(postId);
            if (!"ACTIVE".equals(postStatus)) {
                throw new IllegalArgumentException("참여할 수 없는 모임입니다.");
            }

            // 본인이 작성한 게시글인지 확인
            String creatorId = travelmateMapper.findPostCreatorId(postId);
            if (userId.equals(creatorId)) {
                throw new IllegalArgumentException("본인이 작성한 모임에는 신청할 수 없습니다.");
            }

            // 이미 신청했는지 확인
            String currentStatus = travelmateMapper.findMemberApplicationStatus(postId, userId);
            if (currentStatus != null) {
                switch (currentStatus) {
                    case "PENDING":
                        throw new IllegalArgumentException("이미 참여 신청한 모임입니다.");
                    case "ACCEPTED":
                        throw new IllegalArgumentException("이미 참여 중인 모임입니다.");
                    case "REJECTED":
                        // 거절된 경우 재신청 가능하므로 기존 레코드 삭제 후 새로 생성
                        travelmateMapper.deleteMemberApplication(postId, userId);
                        break;
                }
            }

            // 게시글 제목 조회 (알림용)
            String groupName = travelmateMapper.findPostTitle(postId);

            // 참여 신청 추가
            travelmateMapper.insertMemberApplication(postId, userId, description);

            // 알림 정보 반환
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("needNotification", true);
            result.put("creatorId", creatorId);
            result.put("groupName", groupName);
            result.put("postId", postId);
            result.put("applicantId", userId);
            
            return result;
            
        } catch (IllegalArgumentException e) {
            System.err.println("참여 신청 실패: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("참여 신청 중 오류 발생 - 게시글: " + postId + ", 사용자: " + userId + ", 오류: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("참여 신청에 실패했습니다.", e);
        }
    }

    public List<TravelmateMemberDto> getTravelmateMembers(Long postId) {
        return travelmateMapper.getTravelmateMembers(postId);
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

    public List<TravelmateCommentDto> getTravelmateQuestions(Long postId) {
        List<TravelmateCommentDto> allComments = travelmateMapper.getTravelmateComments(postId);
        Map<Long, TravelmateCommentDto> commentMap = new HashMap<>();
        List<TravelmateCommentDto> parentComments = new ArrayList<>();
        
        for (TravelmateCommentDto comment : allComments) {
            comment.setReplies(new ArrayList<>());
            commentMap.put(comment.getId(), comment);
            
            if (comment.getLevel() == 0) {
                parentComments.add(comment);
            }
        }
        
        for (TravelmateCommentDto comment : allComments) {
            if (comment.getLevel() == 1 && comment.getParentId() != null) {
                TravelmateCommentDto parentComment = commentMap.get(comment.getParentId());
                if (parentComment != null) {
                    parentComment.getReplies().add(comment);
                }
            }
        }
        
        for (TravelmateCommentDto parent : parentComments) {
            parent.getReplies().sort((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()));
        }
        
        parentComments.sort((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()));
        
        return parentComments;
    }

    public void createComment(Long postId, String userId, String content) {
        // 내용 유효성 검증
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("질문 내용을 입력해주세요.");
        }
        
        if (content.length() > 1000) {
            throw new IllegalArgumentException("질문은 1000자 이내로 작성해주세요.");
        }
        
        // 질문 생성 (level 0, parentId null)
        travelmateMapper.insertComment(userId, postId, content.trim(), 0, null);
    }

    public void createReply(Long postId, Long parentId, String userId, String content) {

        // 부모 댓글 존재 여부 확인
        boolean parentExists = travelmateMapper.existsComment(parentId, postId);
        if (!parentExists) {
            throw new IllegalArgumentException("존재하지 않는 질문입니다.");
        }
        
        // 부모 댓글이 level 0인지 확인 (댓글에만 답변 가능)
        Integer parentLevel = travelmateMapper.getCommentLevel(parentId);
        if (parentLevel == null || parentLevel != 0) {
            throw new IllegalArgumentException("질문에만 답변할 수 있습니다.");
        }
        
        // 내용 유효성 검증
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("답변 내용을 입력해주세요.");
        }
        
        if (content.length() > 1000) {
            throw new IllegalArgumentException("답변은 1000자 이내로 작성해주세요.");
        }
        
        // 답변 생성 (level 1, parentId 설정)
        travelmateMapper.insertComment(userId, postId, content.trim(), 1, parentId);
    }

    public void updateComment(Long postId, Long commentId, String userId, String content) {
        // 댓글 존재 여부 및 권한 확인
        TravelmateCommentDto comment = travelmateMapper.getCommentById(commentId);
        if (comment == null) {
            throw new IllegalArgumentException("존재하지 않는 댓글입니다.");
        }
        
        if (!comment.getMateId().equals(postId)) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
        
        if (!comment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("댓글 수정 권한이 없습니다.");
        }
        
        if (comment.getIsDeleted()) {
            throw new IllegalArgumentException("삭제된 댓글은 수정할 수 없습니다.");
        }
        
        if ("BLINDED".equals(comment.getBlindStatus())) {
            throw new IllegalArgumentException("블라인드 처리된 댓글은 수정할 수 없습니다.");
        }
        
        // 내용 유효성 검증
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("댓글 내용을 입력해주세요.");
        }
        
        if (content.length() > 1000) {
            throw new IllegalArgumentException("댓글은 1000자 이내로 작성해주세요.");
        }
        
        // 댓글 수정
        travelmateMapper.updateComment(commentId, content.trim());
    }

    public void deleteComment(Long postId, Long commentId, String userId) {
        // 댓글 존재 여부 및 권한 확인
        TravelmateCommentDto comment = travelmateMapper.getCommentById(commentId);
        if (comment == null) {
            throw new IllegalArgumentException("존재하지 않는 댓글입니다.");
        }
        
        if (!comment.getMateId().equals(postId)) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
        
        if (!comment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("댓글 삭제 권한이 없습니다.");
        }
        
        if (comment.getIsDeleted()) {
            throw new IllegalArgumentException("이미 삭제된 댓글입니다.");
        }
        
        // 대댓글이 있는 경우 확인
        int replyCount = travelmateMapper.getReplyCount(commentId);
        if (replyCount > 0 && comment.getLevel() == 0) {
            // 부모 댓글에 답변이 있는 경우 내용만 변경하고 삭제 표시
            travelmateMapper.softDeleteCommentWithReplies(commentId);
        } else {
            // 답변이 없거나 답변 자체인 경우 완전 소프트 삭제
            travelmateMapper.softDeleteComment(commentId);
        }
    }

    @Transactional
    public void updateTravelmate(Long postId, TravelmateUpdateRequest request, String currentUserId) {
        // 1. 모임 존재 여부 및 권한 확인
        Travelmate existingTravelmate = travelmateMapper.findById(postId);
        if (existingTravelmate == null) {
            throw new IllegalArgumentException("존재하지 않는 모임입니다.");
        }
        
        if (!existingTravelmate.getCreatorId().equals(currentUserId)) {
            throw new IllegalArgumentException("모임 수정 권한이 없습니다.");
        }

        // 2. 모임 기본 정보 업데이트
        TravelmateUpdateDto updateDto = TravelmateUpdateDto.builder()
            .id(postId)
            .title(request.getTitle())
            .simpleDescription(request.getSimpleDescription())
            .description(request.getDescription())
            .applicationDescription(request.getApplicationDescription())
            .backgroundImage(request.getBackgroundImage())
            .thumbnailImage(request.getThumbnailImage())
            .startAt(LocalDateTime.parse(request.getStartAt() + "T00:00:00"))
            .endAt(LocalDateTime.parse(request.getEndAt() + "T23:59:59"))
            .updatedAt(LocalDateTime.now())
            .build();
            
        travelmateMapper.updateTravelmate(updateDto);

        // 3. 기존 연관 데이터 삭제
        travelmateMapper.deleteTravelmateLocations(postId);
        travelmateMapper.deleteTravelmateThemes(postId);
        travelmateMapper.deleteTravelmateStyles(postId);

        // 4. 연관 데이터 등록
        // 지역 등록
        if (request.getLocationIds() != null && !request.getLocationIds().isEmpty()) {
            for (String locationId : request.getLocationIds()) {
                String[] parts = locationId.split("-");
                if (parts.length == 2) {
                    String countryId = parts[0];  // "ALB"
                    int cityId = Integer.parseInt(parts[1]);       // "34" 
                travelmateMapper.insertTravelmateLocation(postId, countryId, cityId);
                }
            }
        }

        // 대상 등록
        if (request.getTargetIds() != null && !request.getTargetIds().isEmpty()) {
            for (Long themeId : request.getTargetIds()) {
                travelmateMapper.insertTravelmateTheme(postId, themeId);
            }
        }

        // 테마 등록
        if (request.getThemeIds() != null && !request.getThemeIds().isEmpty()) {
            for (Long themeId : request.getThemeIds()) {
                travelmateMapper.insertTravelmateTheme(postId, themeId);
            }
        }

        // 스타일 등록
        if (request.getStyleIds() != null && !request.getStyleIds().isEmpty()) {
            for (Character styleId : request.getStyleIds()) {
                travelmateMapper.insertTravelmateStyle(postId, styleId);
            }
        }
    }

    @Transactional
    public Long createTravelmate(TravelmateCreateRequest request, String currentUserId) {
        // 1. 입력값 검증
        validateCreateRequest(request);

        // 2. 모임 기본 정보 생성
        System.out.println("=== 모임 생성 시작 ===");
        TravelmateCreateDto createDto = TravelmateCreateDto.builder()
            .title(request.getTitle())
            .simpleDescription(request.getSimpleDescription())
            .description(request.getDescription())
            .applicationDescription(request.getApplicationDescription())
            .backgroundImage(request.getBackgroundImage())
            .thumbnailImage(request.getThumbnailImage())
            .creatorId(currentUserId)
            .startAt(LocalDateTime.parse(request.getStartAt() + "T00:00:00"))
            .endAt(LocalDateTime.parse(request.getEndAt() + "T23:59:59"))
            .status("ACTIVE")
            .blindStatus("VISIBLE")
            .likeCount(0)
            .memberCount(1)  // 생성자 포함
            .viewCount(0)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
            
        System.out.println("=== 모임 생성 배경:  ==="+createDto.getBackgroundImage());
        // 3. 모임 생성
        travelmateMapper.insertTravelmate(createDto);
        Long travelmateId = createDto.getId(); // MyBatis에서 생성된 ID 반환
        System.out.println("=== 모임 생성 만듬 ===");

        // 4. 연관 데이터 등록
        // 지역 등록
        if (request.getLocationIds() != null && !request.getLocationIds().isEmpty()) {
            for (String locationId : request.getLocationIds()) {
                String[] parts = locationId.split("-");
                if (parts.length == 2) {
                    String countryId = parts[0];  // "ALB"
                    int cityId = Integer.parseInt(parts[1]);       // "34" 
                travelmateMapper.insertTravelmateLocation(travelmateId, countryId, cityId);
                }
            }
        }
        System.out.println("=== 모임 생성 지역 등록 ===");

        // 대상 등록
        if (request.getTargetIds() != null && !request.getTargetIds().isEmpty()) {
            for (Long themeId : request.getTargetIds()) {
                travelmateMapper.insertTravelmateTheme(travelmateId, themeId);
            }
        }
        System.out.println("=== 모임 생성 대상 등록 ===");

        // 테마 등록
        if (request.getThemeIds() != null && !request.getThemeIds().isEmpty()) {
            for (Long themeId : request.getThemeIds()) {
                travelmateMapper.insertTravelmateTheme(travelmateId, themeId);
            }
        }
        System.out.println("=== 모임 생성 테마 등록 ===");

        // 스타일 등록
        if (request.getStyleIds() != null && !request.getStyleIds().isEmpty()) {
            for (Character styleId : request.getStyleIds()) {
                travelmateMapper.insertTravelmateStyle(travelmateId, styleId);
            }
        }
        System.out.println("=== 모임 생성 스타일 등록 ===");

        boolean isCreator = true;
        // 5. 생성자를 멤버로 자동 등록
        travelmateMapper.insertTravelmateMember(travelmateId, currentUserId, isCreator);

        //챗룸 만들기
        commonMapper.insertChatRoom("TRAVELMATE", (Long) travelmateId);

        return travelmateId;
    }



     private void validateCreateRequest(TravelmateCreateRequest request) {
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("모임 제목은 필수입니다.");
        }
        if (request.getSimpleDescription() == null || request.getSimpleDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("모임 한마디는 필수입니다.");
        }
        if (request.getDescription() == null || request.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("모임 설명은 필수입니다.");
        }
        if (request.getApplicationDescription() == null || request.getApplicationDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("모임 신청 안내 메시지는 필수입니다.");
        }
        if (request.getBackgroundImage() == null || request.getBackgroundImage().trim().isEmpty()) {
            throw new IllegalArgumentException("배경 이미지는 필수입니다.");
        }
        if (request.getThumbnailImage() == null || request.getThumbnailImage().trim().isEmpty()) {
            throw new IllegalArgumentException("썸네일 이미지는 필수입니다.");
        }
        if (request.getStartAt() == null || request.getStartAt().trim().isEmpty()) {
            throw new IllegalArgumentException("여행 시작일은 필수입니다.");
        }
        if (request.getEndAt() == null || request.getEndAt().trim().isEmpty()) {
            throw new IllegalArgumentException("여행 종료일은 필수입니다.");
        }
        if (request.getLocationIds() == null || request.getLocationIds().isEmpty()) {
            throw new IllegalArgumentException("지역은 최소 1개 이상 선택해야 합니다.");
        }
    }

    @Transactional
    public void deleteTravelmate(Long travelmateId, String currentUserId) {
        // 1. 여행자모임 존재 여부 및 작성자 확인
        TravelmateResponseDto travelmate = travelmateMapper.selectTravelmateForDelete(travelmateId);
        if (travelmate == null) {
            throw new RuntimeException("여행자모임을 찾을 수 없습니다. ID: " + travelmateId);
        }
        
        // 2. 삭제 권한 확인 (작성자만 삭제 가능)
        if (!travelmate.getCreatorId().equals(currentUserId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다. 작성자만 삭제할 수 있습니다.");
        }
        
        // 3. 관련 데이터 삭제 (순서 중요)
        try {
            // 3-1. 채팅 메시지 삭제
            travelmateMapper.deleteChatMessagesByTravelmateId(travelmateId);
            
            // 3-2. 그룹 회원 삭제 (채팅 참여자)
            travelmateMapper.deleteGroupUsersByTravelmateId(travelmateId);
            
            // 3-3. 채팅방 삭제
            travelmateMapper.deleteChatRoomByTravelmateId(travelmateId);
            
            // 3-4. 댓글 삭제
            travelmateMapper.deleteTravelmateChildComments(travelmateId);
            travelmateMapper.deleteTravelmateParentComments(travelmateId);
            
            // 3-5. 좋아요 정보 삭제
            travelmateMapper.deleteTravelmateLikes(travelmateId);
            
            // 3-6. 신청 정보 삭제
            travelmateMapper.deleteTravelmateApplications(travelmateId);
            
            // 3-7. 여행 테마 삭제
            travelmateMapper.deleteTravelmateThemes(travelmateId);
            
            // 3-8. 여행 스타일 삭제
            travelmateMapper.deleteTravelmateStyles(travelmateId);
            
            // 3-9. 여행 지역 삭제
            travelmateMapper.deleteTravelmateRegions(travelmateId);
            
            // 3-10. 여행자모임 메인 테이블 삭제
            int deletedTravelmate = travelmateMapper.deleteTravelmateById(travelmateId);
            if (deletedTravelmate == 0) {
                throw new RuntimeException("여행자모임 삭제에 실패했습니다.");
            }
            
        } catch (Exception e) {
            throw new RuntimeException("여행자모임 삭제 중 오류가 발생했습니다.", e);
        }
    }

    @Transactional
    public Map<String, Object> processApplication(Long travelmateId, Integer applicationId, String action, String currentUserId) {
        // 1. 해당 여행 모임의 호스트인지 확인
        TravelmatePostDto travelmate = travelmateMapper.getTravelmateById(travelmateId);
        if (travelmate == null) {
            throw new RuntimeException("존재하지 않는 여행 모임입니다.");
        }
        
        if (!travelmate.getCreatorId().equals(currentUserId)) {
            throw new RuntimeException("해당 모임의 호스트만 신청을 처리할 수 있습니다.");
        }
        
        // 2. 신청 정보 확인
        TravelmateApplicationDto application = travelmateMapper.getApplicationById(applicationId);
        if (application == null) {
            throw new RuntimeException("존재하지 않는 신청입니다.");
        }
        
        if (!application.getMateId().equals(travelmateId)) {
            throw new RuntimeException("해당 모임의 신청이 아닙니다.");
        }
        
        if (!application.getStatus().equals("PENDING")) {
            throw new RuntimeException("이미 처리된 신청입니다.");
        }
        
        // 3. 신청 상태 업데이트
        String newStatus = action.equals("accept") ? "ACCEPTED" : "REJECTED";
        travelmateMapper.updateApplicationStatus(applicationId, newStatus, currentUserId);
        
        // 4. 수락인 경우 그룹 멤버에 추가
        if (action.equals("accept")) {
            travelmateMapper.addGroupMember(application.getUserId(), "TRAVELMATE", travelmateId);
        }

        // 5. 알림 정보 반환
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("needNotification", true);
        result.put("action", action);
        result.put("applicantId", application.getUserId());
        result.put("groupName", travelmate.getTitle());
        result.put("groupId", travelmateId);
        result.put("hostId", currentUserId);
        
        return result;
    }
}
