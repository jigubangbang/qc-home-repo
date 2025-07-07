package com.jigubangbang.quest_service.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jigubangbang.quest_service.model.AdminQuestDetailDto;
import com.jigubangbang.quest_service.model.AdminQuestDto;
import com.jigubangbang.quest_service.model.AdminQuestUserDto;
import com.jigubangbang.quest_service.model.BadgeQuestDto;
import com.jigubangbang.quest_service.model.QuestCerti;
import com.jigubangbang.quest_service.model.QuestDto;
import com.jigubangbang.quest_service.model.SimpleBadgeDto;

@Mapper
public interface AdminQuestMapper {
    //조회
    void updateQuestStatus();
    List<AdminQuestDto> selectQuestList(Map<String, Object> params);
    int selectQuestCount(Map<String, Object> params);

    //quest detail 조회
    AdminQuestDetailDto selectQuestDetail(int quest_id);
    int selectQuestCompletedCount(int quest_id);
    int selectQuestInProgressCount(int quest_id);
    int selectQuestGivenUpCount(int quest_id);
    List<AdminQuestUserDto> selectQuestUsers(Map<String, Object> params);
    int selectQuestUsersCount(int quest_id);
    List<String> selectQuestUserImages(int quest_user_id);
    List<SimpleBadgeDto> selectQuestBadges(int quest_id);
    int selectQuestBadgesCount(int quest_id);

 
    public void createQuest(QuestDto quest);
    public int updateQuest(Map<String, Object> params);
    public void deleteQuest(int quest_id);

    public List<QuestCerti> getQuestCertiList(Map<String, Object> params);
    public int countQuestCerti(Map<String, Object> params);

    public QuestCerti getQuestCerti(int quest_user_id);
    public List<String> getQuestCertiImageList(int quest_user_id);

    //public int updateQuestUserApprove(int quest_user_id);
    public int updateQuestUserReject(int quest_user_id);
    public String getQuestUserStatus(int quest_user_id);

    public void deleteQuestImage(int quest_user_id);
    public void updateUserXp(Map<String, Object> params);
    public void updateUserLevel(String user_id);


    //badge 지급
    public List<Integer> getBadgeIdsByQuestId(int quest_id);
    
    public List<Integer> getRequiredQuestsByBadgeId(int badge_id);

    public List<Integer> getCompletedQuestsByUserAndBadge(String user_id, int badge_id);

    public int checkUserHasBadge(String user_id, int badge_id);

    public void insertUserBadge(Map<String, Object> params);
}

