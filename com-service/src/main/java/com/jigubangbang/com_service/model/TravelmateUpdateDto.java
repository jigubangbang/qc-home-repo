package com.jigubangbang.com_service.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TravelmateUpdateDto {
    private Long id;
    private String title;
    private String simpleDescription;
    private String description;
    private String applicationDescription;
    private String backgroundImage;
    private String thumbnailImage;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private LocalDateTime updatedAt;
}
