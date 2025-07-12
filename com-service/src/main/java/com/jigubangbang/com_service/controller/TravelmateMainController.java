package com.jigubangbang.com_service.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jigubangbang.com_service.model.CityDto;
import com.jigubangbang.com_service.model.CountryDto;
import com.jigubangbang.com_service.model.CreateCommentRequest;
import com.jigubangbang.com_service.model.LikedPostsResponse;
import com.jigubangbang.com_service.model.TargetDto;
import com.jigubangbang.com_service.model.ThemeDto;
import com.jigubangbang.com_service.model.TravelStyleDto;
import com.jigubangbang.com_service.model.TravelmateCommentDto;
import com.jigubangbang.com_service.model.TravelmateCreateRequest;
import com.jigubangbang.com_service.model.TravelmateDetailResponse;
import com.jigubangbang.com_service.model.TravelmateListResponse;
import com.jigubangbang.com_service.model.TravelmateMemberDto;
import com.jigubangbang.com_service.model.TravelmateUpdateRequest;
import com.jigubangbang.com_service.service.TravelmateService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping
public class TravelmateMainController {
    @Autowired
    private TravelmateService travelmateService;

    //국가 목록 조회
    @GetMapping("/com/countries")
    public ResponseEntity<List<CountryDto>> getAllCountries() {
        try {
            List<CountryDto> countries = travelmateService.getAllCountries();
            return ResponseEntity.ok(countries);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    //도시 목록 조회
    @GetMapping("/com/cities/{countryId}")
    public ResponseEntity<List<CityDto>> getCitiesByCountryId(@PathVariable String countryId) {
        try {
            List<CityDto> cities = travelmateService.getCitiesByCountryId(countryId);
            return ResponseEntity.ok(cities);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    //filter 정보 조회
    @GetMapping("/com/targets")
    public ResponseEntity<List<TargetDto>> getAllTargets() {
        try {
            List<TargetDto> targets = travelmateService.getAllTargets();
            return ResponseEntity.ok(targets);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/com/themes")
    public ResponseEntity<List<ThemeDto>> getAllThemes() {
        try {
            List<ThemeDto> themes = travelmateService.getAllThemes();
            return ResponseEntity.ok(themes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/com/travel-styles")
    public ResponseEntity<List<TravelStyleDto>> getAllTravelStyles() {
        try {
            List<TravelStyleDto> travelStyles = travelmateService.getAllTravelStyles();
            return ResponseEntity.ok(travelStyles);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/com/travelmate/list")
    public ResponseEntity<TravelmateListResponse> getTravelmateList(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(required = false) String locations,
            @RequestParam(required = false) String targets,
            @RequestParam(required = false) String themes,
            @RequestParam(required = false) String styles,
            @RequestParam(required = false) String continent,
            @RequestParam(defaultValue = "default") String sortOption,
            @RequestParam(defaultValue = "false") boolean showCompleted,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate) {

        // 페이지 설정
        int pageSize = 10;
        int offset = (pageNum - 1) * pageSize;

        // 필터 파라미터 파싱
        List<String> locationList = parseCommaSeparatedString(locations);
        List<Integer> targetList = parseCommaSeparatedIntegerString(targets);
        List<Integer> themeList = parseCommaSeparatedIntegerString(themes);
        List<String> styleList = parseCommaSeparatedString(styles);
        List<String> continentList = parseCommaSeparatedString(continent);


        TravelmateListResponse response = travelmateService.getTravelmateList(
                pageNum,
                pageSize,
                offset,
                locationList,
                targetList,
                themeList,
                styleList,
                continentList,
                sortOption,
                showCompleted,
                search,
                startDate,
                endDate
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user-com/travelmate/likes")
    public ResponseEntity<LikedPostsResponse> getLikedPosts(
            @RequestHeader("User-Id") String userId) {
        
        List<Long> likedPostIds = travelmateService.getLikedPostIds(userId);
        return ResponseEntity.ok(new LikedPostsResponse(likedPostIds));
    }

    
    @PostMapping("/user-com/travelmate/like/{postId}")
    public ResponseEntity<Map<String, Object>> addLike(
            @PathVariable Long postId,
            @RequestHeader("User-Id") String userId) {
        
        try {
            travelmateService.addLike(postId, userId);
            return ResponseEntity.ok(Map.of("success", true, "message", "좋아요가 추가되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/user-com/travelmate/like/{postId}")
    public ResponseEntity<Map<String, Object>> removeLike(
            @PathVariable Long postId,
            @RequestHeader("User-Id") String userId) {
        
        try {
            travelmateService.removeLike(postId, userId);
            return ResponseEntity.ok(Map.of("success", true, "message", "좋아요가 제거되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    private List<String> parseCommaSeparatedString(String str) {
        if (str == null || str.trim().isEmpty()) {
            return List.of();
        }
        return List.of(str.split(","));
    }

    private List<Integer> parseCommaSeparatedIntegerString(String str) {
        if (str == null || str.trim().isEmpty()) {
            return List.of();
        }
        try {
            return List.of(str.split(","))
                    .stream()
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .toList();
        } catch (NumberFormatException e) {
            return List.of();
        }
    }

    @GetMapping("/com/travelmate/{postId}")
    public ResponseEntity<TravelmateDetailResponse> getTravelmateDetail(@PathVariable Long postId) {
        try {
            TravelmateDetailResponse detail = travelmateService.getTravelmateDetail(postId);
            return ResponseEntity.ok(detail);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user-com/travelmate/{postId}/member-status")
    public ResponseEntity<Map<String, Object>> getMemberStatus(
            @PathVariable Long postId,
            @RequestHeader("User-Id") String userId) {
        
        try {
            String status = travelmateService.getMemberStatus(postId, userId);
            return ResponseEntity.ok(Map.of("status", status));
        } catch (Exception e) {
            System.err.println("멤버 상태 조회 중 오류: " + e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("error", "멤버 상태 조회에 실패했습니다."));
        }
    }

    @PostMapping("/user-com/travelmate/{postId}/join")
    public ResponseEntity<Map<String, Object>> joinTravelmate(
            @PathVariable Long postId,
            @RequestBody Map<String, String> request,
            @RequestHeader("User-Id") String userId) {
        
        String description = request.get("description");
        
        try {
            travelmateService.joinTravelmate(postId, userId, description);
            return ResponseEntity.ok(Map.of(
                "success", true, 
                "message", "참여 신청이 완료되었습니다.",
                "status", "PENDING"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/com/travelmate/{postId}/members")
    public ResponseEntity<List<TravelmateMemberDto>> getTravelmateMembers(@PathVariable Long postId) {
        try {
            List<TravelmateMemberDto> members = travelmateService.getTravelmateMembers(postId);
            return ResponseEntity.ok(members);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/com/travelmate/{postId}/comments")
    public ResponseEntity<List<TravelmateCommentDto>> getTravelmateQuestions(@PathVariable Long postId) {
        try {
            List<TravelmateCommentDto> questions = travelmateService.getTravelmateQuestions(postId);
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    
    @PostMapping("/user-com/travelmate/{postId}/comments")
    public ResponseEntity<Map<String, Object>> createQuestion(
            @PathVariable Long postId,
            @RequestBody CreateCommentRequest request,
            @RequestHeader("User-Id") String userId) {
        
        try {
            travelmateService.createComment(postId, userId, request.getContent());
            return ResponseEntity.ok(Map.of("success", true, "message", "질문이 등록되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 답변 등록
    @PostMapping("/user-com/travelmate/{postId}/comments/{parentId}/replies")
    public ResponseEntity<Map<String, Object>> createReply(
            @PathVariable Long postId,
            @PathVariable Long parentId,
            @RequestBody CreateCommentRequest request,
            @RequestHeader("User-Id") String userId) {
        
        try {
            travelmateService.createReply(postId, parentId, userId, request.getContent());
            return ResponseEntity.ok(Map.of("success", true, "message", "답변이 등록되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/user-com/travelmate/{postId}/comments/{commentId}")
    public ResponseEntity<Map<String, Object>> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody CreateCommentRequest request,
            @RequestHeader("User-Id") String userId) {
        
        try {
            travelmateService.updateComment(postId, commentId, userId, request.getContent());
            return ResponseEntity.ok(Map.of("success", true, "message", "댓글이 수정되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


    // 댓글 삭제 (소프트 삭제)
    @DeleteMapping("/user-com/travelmate/{postId}/comments/{commentId}")
    public ResponseEntity<Map<String, Object>> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestHeader("User-Id") String userId) {
        
        try {
            travelmateService.deleteComment(postId, commentId, userId);
            return ResponseEntity.ok(Map.of("success", true, "message", "댓글이 삭제되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/user-com/travelmate/{postId}")
    public ResponseEntity<Map<String, Object>> updateTravelmate(
            @PathVariable("postId") Long postId,
            @RequestBody TravelmateUpdateRequest request,
            @RequestHeader("User-Id") String userId) {
        
        try {
            travelmateService.updateTravelmate(postId, request, userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "모임이 성공적으로 수정되었습니다.");
            response.put("travelmateId", postId);
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", "모임 수정 권한이 없습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "모임 수정에 실패했습니다."));
        }
    }

    @PostMapping("/user-com/travelmate")
    public ResponseEntity<Map<String, Object>> createTravelmate(
            @RequestBody TravelmateCreateRequest request,
            @RequestHeader("User-Id") String userId) {
        
        try {
            Long travelmateId = travelmateService.createTravelmate(request, userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "모임이 성공적으로 생성되었습니다.");
            response.put("travelmateId", travelmateId);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("error", "모임 생성에 실패했습니다.", "detail", e.getMessage()));
        }
    }

    @DeleteMapping("/user-com/travelmate/{travelmateId}")
    public ResponseEntity<Map<String, Object>> deleteTravelmate(
            @PathVariable Long travelmateId,
            @RequestHeader("User-Id") String userId) {
        try {
            travelmateService.deleteTravelmate(travelmateId, userId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "여행자모임이 성공적으로 삭제되었습니다.");
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            System.err.println("권한 오류: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "삭제 권한이 없습니다: " + e.getMessage());
            
            return ResponseEntity.status(403).body(errorResponse);
            
        } catch (RuntimeException e) {
            System.err.println("런타임 오류: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "여행자모임을 찾을 수 없습니다: " + e.getMessage());
            
            return ResponseEntity.status(404).body(errorResponse);
            
        } catch (Exception e) {
            System.err.println("여행자모임 삭제 중 예상치 못한 오류: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "여행자모임 삭제에 실패했습니다: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/user-com/travelmate/{travelmateId}/application/{applicationId}/{action}")
    public ResponseEntity<Map<String, Object>> processApplication(
        @PathVariable Long travelmateId,
        @PathVariable Integer applicationId,  // Long → Integer
        @PathVariable String action,
        @RequestHeader("User-Id") String currentUserId) {
        
        try {
            if (!action.equals("accept") && !action.equals("reject")) {
                return ResponseEntity.badRequest().body(Map.of("error", "잘못된 액션입니다."));
            }
            
            travelmateService.processApplication(travelmateId, applicationId, action, currentUserId);
            
            String message = action.equals("accept") ? "신청을 수락했습니다." : "신청을 거절했습니다.";
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", message
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
