package com.jigubangbang.quest_service.service;

import com.jigubangbang.quest_service.model.RankingDto;
import com.jigubangbang.quest_service.model.UserRankingDto;
import com.jigubangbang.quest_service.repository.RankingMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public int getTotalMember(){
        return rankingMapper.getTotalMember();
    }

    public int getRankingById(String user_id){
        return rankingMapper.getRankingById(user_id);
    }

    public Map<String, Object>  getRankingList(int page, int limit, String search){
        int offset = (page - 1) * limit;

        Map<String, Object> params = new HashMap<>();
        params.put("limit", limit);
        params.put("offset", offset);
        params.put("search", search);

        List<UserRankingDto> rankings = rankingMapper.getRankingList(params);

        int totalCount = rankingMapper.getTotalRankingCount(search);
        
        int totalPages = (int) Math.ceil((double) totalCount / limit);

        for (int i = 0; i<rankings.size(); i++){
            rankings.get(i).setRank(offset+i+1);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("rankings", rankings);
        result.put("totalCount", totalCount);
        result.put("currentPage", page);
        result.put("totalPages", totalPages);

        return result;
    }
    
}