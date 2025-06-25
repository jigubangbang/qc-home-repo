package com.jigubangbang.quest_service.service;

import com.jigubangbang.quest_service.model.RankingDto;
import com.jigubangbang.quest_service.repository.RankingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RankingService {
    @Autowired
    private RankingMapper rankingMapper;


    public RankingDto getWeeklyQuestRanking() {
        return rankingMapper.getWeeklyQuestRanking();
    }

    public RankingDto getWeeklyLevelRanking() {
        return rankingMapper.getWeeklyLevelRanking();
    }

    public RankingDto getTopLevelRanking() {
        return rankingMapper.getTopLevelRanking();
    }

    public RankingDto getTopQuestRanking() {
        return rankingMapper.getTopQuestRanking();
    }
}