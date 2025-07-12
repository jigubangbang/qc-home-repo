package com.jigubangbang.com_service.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TravelmateResponseDto {
    private Long id;
    private String creatorId;
    private String title;
    private String simpleDescription;
    private String description;
    private String applicationDescription;
    private String backgroundImage;
    private String thumbnailImage;
    private String status;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private LocalDateTime createdAt;
    private String blindStatus;
}
