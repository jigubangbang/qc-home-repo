package com.jigubangbang.com_service.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyHostedTravelInfoDto {
    private Integer id;
    private String title;
    private String simpleDescription;
    private String creatorId;
    private String creatorNickname;
    private String enterDescription;
    private String thumbnailImage;
    private LocalDateTime createdAt;
    private String blindStatus;
    private List<Integer> themeIds;
    private Integer likeCount;
    private Integer memberCount;
    private String latestMessage;
    private Integer chatCount;
}
