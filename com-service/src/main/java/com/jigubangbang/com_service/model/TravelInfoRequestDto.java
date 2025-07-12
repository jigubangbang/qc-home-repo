package com.jigubangbang.com_service.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TravelInfoRequestDto {
    private Long id;
    private String title;
    private String simpleDescription;
    private String enterDescription;
    private String creatorId;
    private String thumbnailImage;
    private List<Integer> themeIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
