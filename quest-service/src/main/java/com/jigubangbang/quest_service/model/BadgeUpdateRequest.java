package com.jigubangbang.quest_service.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadgeUpdateRequest {
    private String kor_title;
    private String eng_title;
    private String description;
    private String icon_url;
    private List<Integer> quest_ids;
    
    public boolean hasIconUpdate() {
        return icon_url != null && !icon_url.trim().isEmpty();
    }
    
    public boolean hasQuestUpdates() {
        return quest_ids != null && !quest_ids.isEmpty();
    }
}