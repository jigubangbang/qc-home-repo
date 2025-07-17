package com.jigubangbang.quest_service.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jigubangbang.quest_service.model.AdminBadgeDto;
import com.jigubangbang.quest_service.model.AdminQuestDetailDto;
import com.jigubangbang.quest_service.model.BadgeCreateRequest;
import com.jigubangbang.quest_service.model.BadgeDto;
import com.jigubangbang.quest_service.model.BadgeModalDto;
import com.jigubangbang.quest_service.model.BadgePublicModalDto;
import com.jigubangbang.quest_service.model.BadgeQuestDto;
import com.jigubangbang.quest_service.model.BadgeUpdateRequest;
import com.jigubangbang.quest_service.model.QuestDto;
import com.jigubangbang.quest_service.model.QuestSimpleParticipantDto;
import com.jigubangbang.quest_service.model.UserBadgeDto;

@Mapper
public interface BadgeMapper {
    public List<BadgeDto> getAllBadges(Map<String, Object> params);
    public int getBadgeCount(Map<String, Object> params);

    //모달
    public BadgeModalDto getBadgeModalBase(Map<String, Object> params);
    public BadgePublicModalDto getBadgePublicModalBase(int badge_id);
    public List<QuestDto> getQuestListByBadgeId(int badge_id);
    public int getCompletedQuestCount(Map<String, Object> params);
    public int getAwardedUserCount(int badge_id);
    public List<QuestSimpleParticipantDto> getAwardedUserList(int badge_id);

    public List<String> getQuestTitlesByBadgeId(int id);
    public BadgeDto getBadgeById(int badge_id);
    
    public List<UserBadgeDto> getUserBadgeInfo(String user_id);
    //UserBadgeDto getUserBadgeInfoById(Map<String, Object> params);
    
    public List<UserBadgeDto> getUserBadges(String user_id);
    public UserBadgeDto getUserBadge(Map<String, Object> params);
    public int updateBadgeDisplay(Map<String, Object> params);
    public int insertUserBadge(UserBadgeDto userBadge);

    public List<BadgeQuestDto> getBadgeQuests(int badge_id);
    public int getUserCompletedQuestCount(Map<String, Object> params);

    List<AdminBadgeDto> getAdminBadgeList(@Param("search") String search);

    int insertBadge(BadgeCreateRequest request);
    int insertBadgeQuest(@Param("badgeId") int badgeId, @Param("questId") int questId);
    boolean existsBadgeById(@Param("badgeId") int badgeId);
    Integer findNextAvailableId();
    
    public void deleteBadge(int badge_id);
    void deleteBadgeQuest(int badge_id);
    void deleteBadgeUser(int badge_id);

    //수정폼
    // 뱃지와 연관된 퀘스트 목록 조회
    List<AdminQuestDetailDto> getQuestsByBadgeId(int badge_id);
    // 뱃지 획득 사용자 목록 조회 (최대 10명)
    List<Map<String, Object>> getAwardedUsers(int badge_id);

    //수정
    boolean badgeExists(@Param("badge_id") int badge_id);
    boolean questExists(@Param("quest_id") int quest_id);
    int updateBadgeInfo(@Param("badge_id") int badge_id, @Param("request") BadgeUpdateRequest request);
    int deleteBadgeQuestConnections(@Param("badge_id") int badge_id);
    void insertBadgeQuestConnection(@Param("badge_id") int badge_id, @Param("quest_id") int quest_id);
}
