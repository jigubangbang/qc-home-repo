package com.jigubangbang.quest_service.repository;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.jigubangbang.quest_service.model.QuestDto;

@Mapper
public interface AdminQuestMapper {
    public QuestDto createQuest(QuestDto quest);
    public QuestDto updateQuest(Map<String, Object> params);
    public void deleteQuest(int quest_id);
}
