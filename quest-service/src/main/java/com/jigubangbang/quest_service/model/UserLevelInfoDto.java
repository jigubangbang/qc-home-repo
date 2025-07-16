package com.jigubangbang.quest_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLevelInfoDto {
    private String userId;
    private int currentXp;
    private int currentLevel;
    private int nextLevel;
    private int xpRequiredForNextLevel;
    private int xpNeededForNextLevel;
    private int xpRequiredForCurrentLevel;
    private boolean isMaxLevel;
}
