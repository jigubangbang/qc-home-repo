package com.jigubangbang.quest_service.model;

import java.sql.Timestamp;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminQuestUserDto {
    private int quest_id;
    private int quest_user_id;

    private String user_id;
    private String description;
    private String status;
    private Timestamp started_at;
    private Timestamp completed_at;
    private Timestamp given_up_at;

    private List<String> images;
}
