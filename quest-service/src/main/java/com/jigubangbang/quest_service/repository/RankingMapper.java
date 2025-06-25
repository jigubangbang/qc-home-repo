package com.jigubangbang.quest_service.repository;

import com.jigubangbang.quest_service.model.RankingDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RankingMapper {
    public RankingDto getWeeklyQuestRanking();
    public RankingDto getWeeklyLevelRanking();
    public RankingDto getTopLevelRanking();
    public RankingDto getTopQuestRanking();
}