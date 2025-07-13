package com.jigubangbang.com_service.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {
    private Integer id;
    private Integer boardId;
    private String userId;
    private String title;
    private String content;
    private String type;
    private String icon;
    private Integer viewCount;
    private Integer likeCount;
    private Integer bookmarkCount;
    private Integer commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String blindStatus;
    
    // 추가 정보
    private String creatorNickname;
    private String creatorTravelStyle;
    private String categoryTitle;
    private String thumbnail; // 썸네일 이미지 URL
}
