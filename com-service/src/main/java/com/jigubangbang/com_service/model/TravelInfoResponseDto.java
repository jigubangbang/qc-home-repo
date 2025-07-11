package com.jigubangbang.com_service.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TravelInfoResponseDto {
    private Long id;
    private String title;
    private String simpleDescription;
    private String enterDescription;
    private String thumbnailImage;
    private String creatorId;
    private String blindStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<Integer> themeIds;
    private String themeNames;
}
