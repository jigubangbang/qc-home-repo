package com.jigubangbang.quest_service.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.jigubangbang.quest_service.model.QuestListDto;

@Mapper
public interface QuestMapper {
    public List<QuestListDto> getQuests(Map<String, Object> params);
    public int countQuests();
}
