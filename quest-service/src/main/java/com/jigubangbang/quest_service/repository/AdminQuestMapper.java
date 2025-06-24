package com.jigubangbang.quest_service.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.jigubangbang.quest_service.model.QuestCerti;
import com.jigubangbang.quest_service.model.QuestDto;

@Mapper
public interface AdminQuestMapper {
    public void createQuest(QuestDto quest);
    public int updateQuest(Map<String, Object> params);
    public void deleteQuest(int quest_id);

    public List<QuestCerti> getQuestCertiList(Map<String, Object> params);
    public int countQuestCerti(Map<String, Object> params);

    public QuestCerti getQuestCerti(int quest_user_id);
    public List<String> getQuestCertiImageList(int quest_user_id);

    public int updateQuestUserApprove(int quest_user_id);
    public int updateQuestUserReject(int quest_user_id);

    public void updateUserXp(Map<String, Object> params);
}

