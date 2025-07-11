package com.jigubangbang.com_service.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TravelmateUpdateRequest {
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
