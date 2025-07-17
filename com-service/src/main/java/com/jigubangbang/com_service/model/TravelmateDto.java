package com.jigubangbang.com_service.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TravelmateDto {
    private Long id;
    private String title;
    private String simpleDescription;
    private String thumbnailImage;
    private String creatorNickname;
    private String creatorStyle;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String locationNames;      // "Albania Tirana, Korea Seoul" 형태
    private String themeNames;         // "역사/문화 유적지 탐방 모임, 축제/이벤트 참가 여행 모임" 형태
    private String styleNames;         // "디테일플래너, 슬로우로컬러" 형태
    private String targetNames;        // "여성 전용 모임, 20대 모임" 형태
    private int likeCount;
    private int memberCount;
    private int viewCount;
    private boolean isPublic;
    private String status;
    private String blindStatus;
}
