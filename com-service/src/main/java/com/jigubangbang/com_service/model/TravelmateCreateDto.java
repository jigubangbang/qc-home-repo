package com.jigubangbang.com_service.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TravelmateCreateDto {
    private Long id;
    private String title;
    private String simpleDescription;
    private String description;
    private String applicationDescription;
    private String backgroundImage;
    private String thumbnailImage;
    private String creatorId;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String status;
    private String blindStatus;
    private Integer likeCount;
    private Integer memberCount;
    private Integer viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
