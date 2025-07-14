package com.jigubangbang.com_service.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Travelmate {
    private Long id;
    private String title;
    private String simpleDescription;
    private String description;
    private String applicationDescription;
    private String backgroundImage;
    private String thumbnailImage;
    private String creatorId;
    private String creatorNickname;
    private String creatorProfileImage;
    private String creatorStyle;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String locationNames;
    private String targetNames;
    private String themeNames;
    private String styleNames;
    private Integer likeCount;
    private Integer memberCount;
    private Integer viewCount;
    private String status;
    private String blindStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}