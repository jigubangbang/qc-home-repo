package com.jigubangbang.quest_service.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuestImageDto {
    private int id;
    private int quest_user_id;
    private String image;
}
