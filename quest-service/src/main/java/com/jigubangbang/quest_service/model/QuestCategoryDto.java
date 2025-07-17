package com.jigubangbang.quest_service.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuestCategoryDto {
    private int id;
    private String title;
    private String subtitle;

    private int count_in_progress;
    private int count_completed;
    private int count_total;
}
