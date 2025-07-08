package com.jigubangbang.quest_service.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadgeCreateRequest {
    private Integer id;
    private String kor_title;
    private String eng_title;
    private String description;
    private String icon; // 아이콘 URL
    private Integer difficulty; // 1: Easy, 2: Normal, 3: Hard
    private List<Integer> quest_ids;

}