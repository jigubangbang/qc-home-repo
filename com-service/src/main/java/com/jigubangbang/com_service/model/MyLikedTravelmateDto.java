package com.jigubangbang.com_service.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyLikedTravelmateDto {
     private Integer id;
    private String title;
    private String simpleDescription;
    private String thumbnailImage;
    private String creatorNickname;
    private String creatorStyle;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String locationNames;
    private String themeNames;
    private String styleNames;
    private String targetNames;
    private Integer likeCount;
    private Integer memberCount;
    private Integer viewCount;
    private String status;
    private String blindStatus;
    private LocalDateTime likedAt;
}
