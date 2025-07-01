package com.jigubangbang.quest_service.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.jigubangbang.quest_service.model.QuestDto;
import com.jigubangbang.quest_service.model.QuestParticipantDto;

@Mapper
public interface QuestMapper {
    public List<QuestDto> getQuests(Map<String, Object> params);
    public int countQuests(Map<String, Object> params);

    public List<QuestParticipantDto> getQuestParticipants(int quest_id);
    public int countQuestParticipants(int quest_id);

    public QuestDto selectQuestById(int quest_id);
    
}
