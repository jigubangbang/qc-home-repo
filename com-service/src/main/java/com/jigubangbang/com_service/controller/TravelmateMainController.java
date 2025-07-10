package com.jigubangbang.com_service.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jigubangbang.com_service.model.CityDto;
import com.jigubangbang.com_service.model.CountryDto;
import com.jigubangbang.com_service.model.LikedPostsResponse;
import com.jigubangbang.com_service.model.TargetDto;
import com.jigubangbang.com_service.model.ThemeDto;
import com.jigubangbang.com_service.model.TravelStyleDto;
import com.jigubangbang.com_service.model.TravelmateListResponse;
import com.jigubangbang.com_service.service.TravelmateService;

@RestController
@RequestMapping("/com")
public class TravelmateMainController {
    @Autowired
    private TravelmateService travelmateService;

    @GetMapping("/test")
    public String test(){
        return "good";
    }

    //국가 목록 조회
    @GetMapping("/countries")
    public ResponseEntity<List<CountryDto>> getAllCountries() {
        try {
            List<CountryDto> countries = travelmateService.getAllCountries();
            return ResponseEntity.ok(countries);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    //도시 목록 조회
    @GetMapping("/cities/{countryId}")
    public ResponseEntity<List<CityDto>> getCitiesByCountryId(@PathVariable String countryId) {
        try {
            List<CityDto> cities = travelmateService.getCitiesByCountryId(countryId);
            return ResponseEntity.ok(cities);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    //filter 정보 조회
    @GetMapping("/targets")
    public ResponseEntity<List<TargetDto>> getAllTargets() {
        try {
            List<TargetDto> targets = travelmateService.getAllTargets();
            return ResponseEntity.ok(targets);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeDto>> getAllThemes() {
        try {
            List<ThemeDto> themes = travelmateService.getAllThemes();
            return ResponseEntity.ok(themes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/travel-styles")
    public ResponseEntity<List<TravelStyleDto>> getAllTravelStyles() {
        try {
            List<TravelStyleDto> travelStyles = travelmateService.getAllTravelStyles();
            return ResponseEntity.ok(travelStyles);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/list")
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

        System.out.println("받은 startDate: " + startDate);
    System.out.println("받은 endDate: " + endDate);

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

    @GetMapping("/likes")
    public ResponseEntity<LikedPostsResponse> getLikedPosts() {
        //인증 못하면
        // if (authentication == null || !authentication.isAuthenticated()) {
        //     return ResponseEntity.ok(new LikedPostsResponse(List.of()));
        // }

        // String userId = authentication.getName();
        //#NeedToChange
        String userId="aaa";
        List<Long> likedPostIds = travelmateService.getLikedPostIds(userId);
        
        return ResponseEntity.ok(new LikedPostsResponse(likedPostIds));
    }

    @PostMapping("/like/{postId}")
    public ResponseEntity<Map<String, Object>> addLike(
            @PathVariable Long postId
            ) {
        
        // if (authentication == null || !authentication.isAuthenticated()) {
        //     return ResponseEntity.status(401).body(Map.of("error", "로그인이 필요합니다."));
        // }

        // String userId = authentication.getName();
        //#NeedToChange
        String userId="aaa";
        try {
            travelmateService.addLike(postId, userId);
            return ResponseEntity.ok(Map.of("success", true, "message", "좋아요가 추가되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/like/{postId}")
    public ResponseEntity<Map<String, Object>> removeLike(
            @PathVariable Long postId
           ) {
        
        // if (authentication == null || !authentication.isAuthenticated()) {
        //     return ResponseEntity.status(401).body(Map.of("error", "로그인이 필요합니다."));
        // }

        //String userId = authentication.getName();
        //#NeedToChange
        String userId= "aaa";
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
}
