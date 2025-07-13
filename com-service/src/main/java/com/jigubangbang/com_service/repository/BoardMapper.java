package com.jigubangbang.com_service.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jigubangbang.com_service.model.BoardBookmark;
import com.jigubangbang.com_service.model.BoardCommentDto;
import com.jigubangbang.com_service.model.BoardDto;
import com.jigubangbang.com_service.model.BoardInsertDto;
import com.jigubangbang.com_service.model.BoardLike;
import com.jigubangbang.com_service.model.BoardPostDto;

@Mapper
public interface BoardMapper {
    //조회
    List<BoardDto> getBoardList(Map<String, Object> params);
    int getBoardCount(Map<String, Object> params);
    BoardDto getBoardDetail(Integer postId);
    void incrementViewCount(@Param("id") int id);
    List<String> getBoardImages(@Param("postId") Integer postId);

    //북마크
    boolean existsByPostIdAndUserId(@Param("postId") int postId, @Param("userId") String userId);
    int countByPostId(@Param("postId") int postId);
    void insert(BoardBookmark bookmark);
    void deleteByPostIdAndUserId(@Param("postId") int postId, @Param("userId") String userId);
    boolean existsById(@Param("id") int id);
    void incrementBookmarkCount(@Param("id") int id);
    void decrementBookmarkCount(@Param("id") int id);
    List<Integer> findPostIdsByUserId(@Param("userId") String userId);

    //좋아요
    boolean existsLikeByPostIdAndUserId(@Param("postId") int postId, @Param("userId") String userId);
    int countLikesByPostId(@Param("postId") int postId);
    List<Integer> findLikedPostIdsByUserId(@Param("userId") String userId);
    void insertLike(BoardLike like);
    void deleteLikeByPostIdAndUserId(@Param("postId") int postId, @Param("userId") String userId);
    void incrementLikeCount(@Param("id") int id);
    void decrementLikeCount(@Param("id") int id);

    //댓글
    List<BoardCommentDto> getBoardComments(@Param("postId") Integer postId);
    void insertBoardComment(@Param("userId") String userId,
                           @Param("postId") Integer postId,
                           @Param("content") String content,
                           @Param("level") Integer level,
                           @Param("parentId") Integer parentId);
    boolean existsBoardPost(@Param("postId") Integer postId);
    boolean existsBoardComment(@Param("commentId") Integer commentId, 
                              @Param("postId") Integer postId);
    Integer getBoardCommentLevel(@Param("commentId") Integer commentId);
    BoardCommentDto getBoardCommentById(@Param("commentId") Integer commentId);
    void updateBoardComment(@Param("commentId") Integer commentId, 
                           @Param("content") String content);
    void softDeleteBoardComment(@Param("commentId") Integer commentId);
    void softDeleteBoardCommentWithReplies(@Param("commentId") Integer commentId);
    int getBoardReplyCount(@Param("parentId") Integer parentId);

    //게시판
    void insertBoard(BoardInsertDto insertDto);
    void insertBoardImage(@Param("postId") Integer postId,
                         @Param("imageUrl") String imageUrl);
    BoardPostDto getBoardPostById(@Param("postId") Integer postId);
    void updateBoard(@Param("postId") Integer postId,
                    @Param("boardId") Integer boardId,
                    @Param("title") String title,
                    @Param("content") String content);
    void deleteBoardImages(@Param("postId") Integer postId);
}
