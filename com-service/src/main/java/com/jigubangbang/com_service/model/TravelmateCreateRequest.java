package com.jigubangbang.com_service.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TravelmateCreateRequest {
    private String title;
    private String simpleDescription;
    private String description;
    private String applicationDescription;
    private String backgroundImage;
    private String thumbnailImage;
    private String startAt;
    private String endAt;
    private List<String> locationIds;
    private List<Long> targetIds;
    private List<Long> themeIds;
    private List<Character> styleIds;
}
