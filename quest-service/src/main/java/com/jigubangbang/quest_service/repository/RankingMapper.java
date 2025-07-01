package com.jigubangbang.quest_service.repository;

import com.jigubangbang.quest_service.model.RankingDto;
import com.jigubangbang.quest_service.model.UserRankingDto;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RankingMapper {
    public RankingDto getWeeklyQuestRanking();
    public RankingDto getWeeklyLevelRanking();
    public RankingDto getTopLevelRanking();
    public RankingDto getTopQuestRanking();
    
    public int getTotalMember();
    public int getRankingById(String user_id);

    public List<UserRankingDto> getRankingList(Map<String, Object> params);
    public int getTotalRankingCount(String search);
}