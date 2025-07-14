package com.jigubangbang.com_service.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.jigubangbang.com_service.model.BoardBookmark;
import com.jigubangbang.com_service.model.BoardCommentDto;
import com.jigubangbang.com_service.model.BoardCreateResponse;
import com.jigubangbang.com_service.model.BoardDto;
import com.jigubangbang.com_service.model.BoardInsertDto;
import com.jigubangbang.com_service.model.BoardLike;
import com.jigubangbang.com_service.model.BoardListResponse;
import com.jigubangbang.com_service.model.BoardPostDto;
import com.jigubangbang.com_service.model.BookmarkResponse;
import com.jigubangbang.com_service.model.BookmarkStatusResponse;
import com.jigubangbang.com_service.model.CreateBoardRequest;
import com.jigubangbang.com_service.model.LikeResponse;
import com.jigubangbang.com_service.model.LikeStatusResponse;
import com.jigubangbang.com_service.model.UpdateBoardRequest;
import com.jigubangbang.com_service.repository.BoardMapper;

@Service
public class BoardService {
    @Autowired
    private BoardMapper boardMapper;

    
    public BoardListResponse getBoardList(int pageNum, String category, String sortOption, String search, int limit, String travelStyleId) {
        // 페이지 설정
        int pageSize = limit;
        int offset = (pageNum - 1) * pageSize;

        // 파라미터 맵 구성
        Map<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("limit", pageSize);
        params.put("search", search);
        
        // 카테고리 처리 (배열 형태로 받은 경우)
       if (category != null && !category.trim().isEmpty()) {
            try {
                // category가 "0,1,2" 형태의 문자열인 경우 처리
                if (category.contains(",")) {
                    String[] categoryIds = category.split(",");
                    // 0이 포함되어 있으면 전체 조회이므로 카테고리 필터링 안함
                    boolean hasZero = Arrays.stream(categoryIds).anyMatch(id -> "0".equals(id.trim()));
                    if (!hasZero) {
                        params.put("categoryIds", categoryIds);
                    }
                } else {
                    // 단일 카테고리인 경우
                    Integer categoryId = Integer.parseInt(category.trim());
                    // 0이면 전체 조회이므로 카테고리 필터링 안함
                    if (categoryId != 0) {
                        params.put("boardId", categoryId);
                    }
                }
            } catch (NumberFormatException e) {
                // 카테고리가 문자열인 경우 카테고리 이름으로 검색
                params.put("categoryTitle", category);
            }
        }

        if (travelStyleId != null && !travelStyleId.trim().isEmpty()) {
            try {
                if (travelStyleId.contains(",")) {
                    // "1,2,3" 형태의 문자열인 경우
                    String[] travelStyleIds = travelStyleId.split(",");
                    // 빈 문자열이나 공백 제거
                    List<String> validIds = Arrays.stream(travelStyleIds)
                        .map(String::trim)
                        .filter(id -> !id.isEmpty())
                        .collect(Collectors.toList());
                    
                    if (!validIds.isEmpty()) {
                        params.put("travelStyleIds", validIds.toArray(new String[0]));
                    }
                } else {
                    // 단일 여행스타일인 경우
                    String singleStyleId = travelStyleId.trim();
                    if (!singleStyleId.isEmpty()) {
                        params.put("travelStyleId", singleStyleId);
                    }
                }
            } catch (Exception e) {
                // 여행스타일 ID 파싱 오류 시 무시
                System.err.println("Invalid travelStyleId format: " + travelStyleId);
            }
        }
        
        // 정렬 옵션 처리
        String orderBy = getOrderByClause(sortOption);
        params.put("orderBy", orderBy);

        // 게시글 목록 조회
        List<BoardDto> posts = boardMapper.getBoardList(params);
        
        // 전체 게시글 수 조회
        int totalPosts = boardMapper.getBoardCount(params);
        
        // 페이지 정보 계산
        int totalPages = (int) Math.ceil((double) totalPosts / pageSize);
        boolean hasNext = pageNum < totalPages;
        boolean hasPrevious = pageNum > 1;

        return BoardListResponse.builder()
                .posts(posts)
                .currentPage(pageNum)
                .totalPages(totalPages)
                .totalPosts(totalPosts)
                .hasNext(hasNext)
                .hasPrevious(hasPrevious)
                .build();
    }

    private String getOrderByClause(String sortOption) {
        switch (sortOption.toLowerCase()) {
            case "latest":
                return "bp.created_at DESC";
            case "popular":
                return "(bp.like_count + bp.bookmark_count + bp.view_count) DESC, bp.created_at DESC";
            case "views":
                return "bp.view_count DESC, bp.created_at DESC";
            case "likes":
                return "bp.like_count DESC, bp.created_at DESC";
            case "bookmarks":
                return "bp.bookmark_count DESC, bp.created_at DESC";
            default:
                return "bp.created_at DESC"; 
        }
    }

   public BoardDto getBoardPost(int postId) {
        // 게시글 존재 여부 확인
        if (!boardMapper.existsById(postId)) {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }
        
        // 조회수 증가
        boardMapper.incrementViewCount(postId);
        
        // 게시글 조회
        BoardDto post = boardMapper.getBoardDetail(postId);
        if (post == null) {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }
        
        return post;
    }

    // 게시글 정보만 조회 (조회수 증가 안함)
    public BoardDto getBoardPostInfo(int postId) {
        // 게시글 존재 여부 확인
        if (!boardMapper.existsById(postId)) {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }
        
        // 게시글 조회 (조회수 증가 없음)
        BoardDto post = boardMapper.getBoardDetail(postId);
        if (post == null) {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }
        
        return post;
    }

    public List<String> getBoardImages(Integer postId) {
        return boardMapper.getBoardImages(postId);
    }
    //북마크
    public BookmarkStatusResponse getBookmarkStatus(int postId, String userId) {
        // 게시글 존재 여부 확인
        if (!boardMapper.existsById(postId)) {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }
        
        // 북마크 상태 확인
        boolean isBookmarked = boardMapper.existsByPostIdAndUserId(postId, userId);
        
        // 총 북마크 수 조회
        int bookmarkCount = boardMapper.countByPostId(postId);
        
        return BookmarkStatusResponse.builder()
                .isBookmarked(isBookmarked)
                .bookmarkCount(bookmarkCount)
                .build();
    }

    @Transactional
    public BookmarkResponse addBookmark(int postId, String userId) {
        // 게시글 존재 여부 확인
        if (!boardMapper.existsById(postId)) {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }
        
        // 이미 북마크되어 있는지 확인
        if (boardMapper.existsByPostIdAndUserId(postId, userId)) {
            throw new RuntimeException("이미 북마크된 게시글입니다.");
        }
        
        // 북마크 추가
        BoardBookmark bookmark = BoardBookmark.builder()
                .postId(postId)
                .userId(userId)
                .build();
        boardMapper.insert(bookmark);
        boardMapper.incrementBookmarkCount(postId);
        
        // 업데이트된 북마크 수 조회
        int bookmarkCount = boardMapper.countByPostId(postId);
        
        return BookmarkResponse.builder()
                .success(true)
                .message("북마크가 추가되었습니다.")
                .bookmarkCount(bookmarkCount)
                .build();
    }
    
    @Transactional
    public BookmarkResponse removeBookmark(int postId, String userId) {
        // 게시글 존재 여부 확인
        if (!boardMapper.existsById(postId)) {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }
        
        // 북마크가 존재하는지 확인
        if (!boardMapper.existsByPostIdAndUserId(postId, userId)) {
            throw new RuntimeException("북마크를 찾을 수 없습니다.");
        }
        
        // 북마크 삭제
        boardMapper.deleteByPostIdAndUserId(postId, userId);
        boardMapper.decrementBookmarkCount(postId);
        
        // 업데이트된 북마크 수 조회
        int bookmarkCount = boardMapper.countByPostId(postId);
        
        return BookmarkResponse.builder()
                .success(true)
                .message("북마크가 삭제되었습니다.")
                .bookmarkCount(bookmarkCount)
                .build();
    }

    public List<Integer> getBookmarkedPostIds(String userId) {
        return boardMapper.findPostIdsByUserId(userId);
    }

    public LikeStatusResponse getLikeStatus(int postId, String userId) {
        // 게시글 존재 여부 확인
        if (!boardMapper.existsById(postId)) {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }
        
        // 좋아요 상태 확인
        boolean isLiked = boardMapper.existsLikeByPostIdAndUserId(postId, userId);
        
        // 총 좋아요 수 조회
        int likeCount = boardMapper.countLikesByPostId(postId);
        
        return LikeStatusResponse.builder()
                .isLiked(isLiked)
                .likeCount(likeCount)
                .build();
    }
    
    public List<Integer> getLikedPostIds(String userId) {
        return boardMapper.findLikedPostIdsByUserId(userId);
    }
    
    @Transactional
    public LikeResponse addLike(int postId, String userId) {
        // 게시글 존재 여부 확인
        if (!boardMapper.existsById(postId)) {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }
        
        // 이미 좋아요되어 있는지 확인
        if (boardMapper.existsLikeByPostIdAndUserId(postId, userId)) {
            throw new RuntimeException("이미 좋아요한 게시글입니다.");
        }
        
        // 좋아요 추가
        BoardLike like = BoardLike.builder()
                .postId(postId)
                .userId(userId)
                .build();
        boardMapper.insertLike(like);
        boardMapper.incrementLikeCount(postId);
        
        // 업데이트된 좋아요 수 조회
        int likeCount = boardMapper.countLikesByPostId(postId);
        
        return LikeResponse.builder()
                .success(true)
                .message("좋아요가 추가되었습니다.")
                .likeCount(likeCount)
                .build();
    }
    
    @Transactional
    public LikeResponse removeLike(int postId, String userId) {
        // 게시글 존재 여부 확인
        if (!boardMapper.existsById(postId)) {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }
        
        // 좋아요가 존재하는지 확인
        if (!boardMapper.existsLikeByPostIdAndUserId(postId, userId)) {
            throw new RuntimeException("좋아요를 찾을 수 없습니다.");
        }
        
        // 좋아요 삭제
        boardMapper.deleteLikeByPostIdAndUserId(postId, userId);
        boardMapper.decrementLikeCount(postId);
        
        // 업데이트된 좋아요 수 조회
        int likeCount = boardMapper.countLikesByPostId(postId);
        
        return LikeResponse.builder()
                .success(true)
                .message("좋아요가 삭제되었습니다.")
                .likeCount(likeCount)
                .build();
    }

    //댓글 comment
    public List<BoardCommentDto> getBoardComments(Integer postId) {
        List<BoardCommentDto> allComments = boardMapper.getBoardComments(postId);
        Map<Integer, BoardCommentDto> commentMap = new HashMap<>();
        List<BoardCommentDto> parentComments = new ArrayList<>();
        
        for (BoardCommentDto comment : allComments) {
            comment.setReplies(new ArrayList<>());
            commentMap.put(comment.getId(), comment);
            
            if (comment.getLevel() == 0) {
                parentComments.add(comment);
            }
        }
        
        for (BoardCommentDto comment : allComments) {
            if (comment.getLevel() == 1 && comment.getParentId() != null) {
                BoardCommentDto parentComment = commentMap.get(comment.getParentId());
                if (parentComment != null) {
                    parentComment.getReplies().add(comment);
                }
            }
        }
        
        for (BoardCommentDto parent : parentComments) {
            parent.getReplies().sort((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()));
        }
        
        parentComments.sort((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()));
        
        return parentComments;
    }

    public Map<String, Object> createBoardComment(Integer postId, String userId, String content) {
        // 내용 유효성 검증
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("댓글 내용을 입력해주세요.");
        }
        
        if (content.length() > 1000) {
            throw new IllegalArgumentException("댓글은 1000자 이내로 작성해주세요.");
        }
        
        // 게시글 존재 여부 확인
        boolean postExists = boardMapper.existsBoardPost(postId);
        if (!postExists) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }

         // 게시글 작성자 정보 조회 (알림용)
        String postAuthorId = boardMapper.getBoardPostAuthor(postId);
            
        // 댓글 생성 (level 0, parentId null)
        boardMapper.insertBoardComment(userId, postId, content.trim(), 0, null);

        // 알림 정보 반환
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("needNotification", !userId.equals(postAuthorId)); // 본인 댓글은 알림 X
        result.put("postAuthorId", postAuthorId);
        result.put("commenterId", userId);
        
        return result;
    }

    public Map<String, Object> createBoardReply(Integer postId, Integer parentId, String userId, String content) {
        // 부모 댓글 존재 여부 확인
        boolean parentExists = boardMapper.existsBoardComment(parentId, postId);
        if (!parentExists) {
            throw new IllegalArgumentException("존재하지 않는 댓글입니다.");
        }
        
        // 부모 댓글이 level 0인지 확인 (댓글에만 답변 가능)
        Integer parentLevel = boardMapper.getBoardCommentLevel(parentId);
        if (parentLevel == null || parentLevel != 0) {
            throw new IllegalArgumentException("댓글에만 답변할 수 있습니다.");
        }
        
        // 내용 유효성 검증
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("답변 내용을 입력해주세요.");
        }
        
        if (content.length() > 1000) {
            throw new IllegalArgumentException("답변은 1000자 이내로 작성해주세요.");
        }

        String parentCommentAuthorId = boardMapper.getBoardCommentAuthor(parentId);
        
        // 답변 생성 (level 1, parentId 설정)
        boardMapper.insertBoardComment(userId, postId, content.trim(), 1, parentId);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("needNotification", !userId.equals(parentCommentAuthorId)); 
        result.put("parentCommentAuthorId", parentCommentAuthorId);
        result.put("replierId", userId);
        
        return result;
    }

    public void updateBoardComment(Integer postId, Integer commentId, String userId, String content) {
        // 댓글 존재 여부 및 권한 확인
        BoardCommentDto comment = boardMapper.getBoardCommentById(commentId);
        if (comment == null) {
            throw new IllegalArgumentException("존재하지 않는 댓글입니다.");
        }
        
        if (!comment.getPostId().equals(postId)) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
        
        if (!comment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("댓글 수정 권한이 없습니다.");
        }
        
        if (comment.getIsDeleted()) {
            throw new IllegalArgumentException("삭제된 댓글은 수정할 수 없습니다.");
        }
        
        if ("BLINDED".equals(comment.getBlindStatus())) {
            throw new IllegalArgumentException("블라인드 처리된 댓글은 수정할 수 없습니다.");
        }
        
        // 내용 유효성 검증
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("댓글 내용을 입력해주세요.");
        }
        
        if (content.length() > 1000) {
            throw new IllegalArgumentException("댓글은 1000자 이내로 작성해주세요.");
        }
        
        // 댓글 수정
        boardMapper.updateBoardComment(commentId, content.trim());
    }

    public void deleteBoardComment(Integer postId, Integer commentId, String userId) {
        // 댓글 존재 여부 및 권한 확인
        BoardCommentDto comment = boardMapper.getBoardCommentById(commentId);
        if (comment == null) {
            throw new IllegalArgumentException("존재하지 않는 댓글입니다.");
        }
        
        if (!comment.getPostId().equals(postId)) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
        
        if (!comment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("댓글 삭제 권한이 없습니다.");
        }
        
        if (comment.getIsDeleted()) {
            throw new IllegalArgumentException("이미 삭제된 댓글입니다.");
        }
        
        // 대댓글이 있는 경우 확인
        int replyCount = boardMapper.getBoardReplyCount(commentId);
        if (replyCount > 0 && comment.getLevel() == 0) {
            // 부모 댓글에 답변이 있는 경우 내용만 변경하고 삭제 표시
            boardMapper.softDeleteBoardCommentWithReplies(commentId);
        } else {
            // 답변이 없거나 답변 자체인 경우 완전 소프트 삭제
            boardMapper.softDeleteBoardComment(commentId);
        }
    }

    //게시판
    public BoardCreateResponse createBoard(String userId, CreateBoardRequest request) {
        // 유효성 검사
        validateBoardRequest(request.getTitle(), request.getContent(), request.getBoardId());
        
        // 게시글 생성
        BoardInsertDto insertDto = new BoardInsertDto();
        insertDto.setUserId(userId);
        insertDto.setBoardId(request.getBoardId());
        insertDto.setTitle(request.getTitle().trim());
        insertDto.setContent(request.getContent().trim());
        
        // 게시글 생성
        boardMapper.insertBoard(insertDto);
        
        // 생성된 ID 가져오기
        Integer postId = insertDto.getId();
        
        // 이미지가 있으면 저장
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            for (String imageUrl : request.getImages()) {
                if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                    boardMapper.insertBoardImage(postId, imageUrl.trim());
                }
            }
        }
        
        return new BoardCreateResponse(postId, "게시글이 성공적으로 작성되었습니다.");
    }
    
    public void updateBoard(String userId, Integer postId, UpdateBoardRequest request) {
        // 게시글 존재 여부 및 권한 확인
        BoardPostDto existingPost = boardMapper.getBoardPostById(postId);
        if (existingPost == null) {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }
        
        if (!existingPost.getUserId().equals(userId)) {
            throw new IllegalArgumentException("게시글 수정 권한이 없습니다.");
        }
        
        // 유효성 검사
        validateBoardRequest(request.getTitle(), request.getContent(), request.getBoardId());
        
        // 게시글 정보 업데이트
        boardMapper.updateBoard(
            postId,
            request.getBoardId(),
            request.getTitle().trim(),
            request.getContent().trim()
        );
        
        // 기존 이미지 삭제
        boardMapper.deleteBoardImages(postId);
        
        // 새 이미지들 저장
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            for (String imageUrl : request.getImages()) {
                if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                    boardMapper.insertBoardImage(postId, imageUrl.trim());
                }
            }
        }
    }
    
    private void validateBoardRequest(String title, String content, Integer boardId) {
        // 제목 검증
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("제목은 필수입니다.");
        }
        if (title.length() > 100) {
            throw new IllegalArgumentException("제목은 100자를 초과할 수 없습니다.");
        }
        
        // 내용 검증
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("내용은 필수입니다.");
        }
        if (content.length() > 5000) {
            throw new IllegalArgumentException("내용은 5000자를 초과할 수 없습니다.");
        }
        
        // 카테고리 검증
        if (boardId == null) {
            throw new IllegalArgumentException("카테고리는 필수입니다.");
        }
        if (boardId < 1 || boardId > 4) {
            throw new IllegalArgumentException("올바르지 않은 카테고리입니다.");
        }
    }

    public BoardListResponse getMyPosts(String userId, int pageNum, String sortOption, int limit) {
        // 페이지 설정
        int pageSize = limit;
        int offset = (pageNum - 1) * pageSize;

        // 파라미터 맵 구성
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("offset", offset);
        params.put("limit", pageSize);
        
        // 정렬 옵션 처리
        String orderBy = getOrderByClause(sortOption);
        params.put("orderBy", orderBy);

        // 내가 쓴 글 목록 조회
        List<BoardDto> posts = boardMapper.getMyPosts(params);
        
        // 전체 게시글 수 조회
        int totalPosts = boardMapper.getMyPostsCount(params);
        
        // 페이지 정보 계산
        int totalPages = (int) Math.ceil((double) totalPosts / pageSize);
        boolean hasNext = pageNum < totalPages;
        boolean hasPrevious = pageNum > 1;

        return BoardListResponse.builder()
                .posts(posts)
                .currentPage(pageNum)
                .totalPages(totalPages)
                .totalPosts(totalPosts)
                .hasNext(hasNext)
                .hasPrevious(hasPrevious)
                .build();
    }

    // 내가 댓글 단 글 조회 (페이지네이션, 정렬 지원)
    public Map<String, Object> getMyCommentedPosts(String userId, int pageNum, String sortOption, int limit) {
        // 페이지 설정
        int pageSize = limit;
        int offset = (pageNum - 1) * pageSize;

        // 파라미터 맵 구성
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("offset", offset);
        params.put("limit", pageSize);
        
        // 정렬 옵션 처리 (댓글 작성 시간 기준)
        String orderBy = getCommentOrderByClause(sortOption);
        params.put("orderBy", orderBy);

        // 내가 댓글 단 글 목록 조회 (댓글 내용 포함)
        List<Map<String, Object>> comments = boardMapper.getMyCommentedPosts(params);
        
        // 전체 개수 조회
        int totalComments = boardMapper.getMyCommentedPostsCount(params);
        
        // 페이지 정보 계산
        int totalPages = (int) Math.ceil((double) totalComments / pageSize);
        boolean hasNext = pageNum < totalPages;
        boolean hasPrevious = pageNum > 1;

        Map<String, Object> result = new HashMap<>();
        result.put("comments", comments);
        result.put("currentPage", pageNum);
        result.put("totalPages", totalPages);
        result.put("totalComments", totalComments);
        result.put("hasNext", hasNext);
        result.put("hasPrevious", hasPrevious);
        
        return result;
    }

    // 북마크한 글 조회 (페이지네이션, 정렬 지원)
    public Map<String, Object> getBookmarkedPosts(String userId, int pageNum, String sortOption, int limit) {
        // 페이지 설정
        int pageSize = limit;
        int offset = (pageNum - 1) * pageSize;

        // 파라미터 맵 구성
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("offset", offset);
        params.put("limit", pageSize);
        
        // 정렬 옵션 처리 (북마크 시간 기준)
        String orderBy = getBookmarkOrderByClause(sortOption);
        params.put("orderBy", orderBy);

        // 북마크한 글 ID 목록 조회
        List<Integer> bookmarkedPostIds = boardMapper.getBookmarkedPostIds(params);
        
        // 전체 북마크 개수 조회
        int totalBookmarks = boardMapper.getBookmarkedPostsCount(params);
        
        // 페이지 정보 계산
        int totalPages = (int) Math.ceil((double) totalBookmarks / pageSize);
        boolean hasNext = pageNum < totalPages;
        boolean hasPrevious = pageNum > 1;

        Map<String, Object> result = new HashMap<>();
        result.put("bookmarkedPostIds", bookmarkedPostIds);
        result.put("currentPage", pageNum);
        result.put("totalPages", totalPages);
        result.put("totalBookmarks", totalBookmarks);
        result.put("hasNext", hasNext);
        result.put("hasPrevious", hasPrevious);
        
        return result;
    }

    // 좋아요한 글 조회 (페이지네이션, 정렬 지원)
    public Map<String, Object> getLikedPosts(String userId, int pageNum, String sortOption, int limit) {
        // 페이지 설정
        int pageSize = limit;
        int offset = (pageNum - 1) * pageSize;

        // 파라미터 맵 구성
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("offset", offset);
        params.put("limit", pageSize);
        
        // 정렬 옵션 처리 (좋아요 시간 기준)
        String orderBy = getLikeOrderByClause(sortOption);
        params.put("orderBy", orderBy);

        // 좋아요한 글 ID 목록 조회
        List<Integer> likedPostIds = boardMapper.getLikedPostIds(params);
        
        // 전체 좋아요 개수 조회
        int totalLikes = boardMapper.getLikedPostsCount(params);
        
        // 페이지 정보 계산
        int totalPages = (int) Math.ceil((double) totalLikes / pageSize);
        boolean hasNext = pageNum < totalPages;
        boolean hasPrevious = pageNum > 1;

        Map<String, Object> result = new HashMap<>();
        result.put("likedPostIds", likedPostIds);
        result.put("currentPage", pageNum);
        result.put("totalPages", totalPages);
        result.put("totalLikes", totalLikes);
        result.put("hasNext", hasNext);
        result.put("hasPrevious", hasPrevious);
        
        return result;
    }

    // 댓글 정렬 옵션 처리
    private String getCommentOrderByClause(String sortOption) {
        switch (sortOption.toLowerCase()) {
            case "latest":
                return "bc.created_at DESC";
            case "oldest":
                return "bc.created_at ASC";
            case "popular":
                return "(bp.like_count + bp.bookmark_count + bp.view_count) DESC, bc.created_at DESC";
            default:
                return "bc.created_at DESC";
        }
    }

    // 북마크 정렬 옵션 처리
    private String getBookmarkOrderByClause(String sortOption) {
        switch (sortOption.toLowerCase()) {
            case "latest":
                return "bb.created_at DESC";
            case "oldest":
                return "bb.created_at ASC";
            case "popular":
                return "(bp.like_count + bp.bookmark_count + bp.view_count) DESC, bb.created_at DESC";
            default:
                return "bb.created_at DESC";
        }
    }

    // 좋아요 정렬 옵션 처리
    private String getLikeOrderByClause(String sortOption) {
        switch (sortOption.toLowerCase()) {
            case "latest":
                return "bl.created_at DESC";
            case "oldest":
                return "bl.created_at ASC";
            case "popular":
                return "(bp.like_count + bp.bookmark_count + bp.view_count) DESC, bl.created_at DESC";
            default:
                return "bl.created_at DESC";
        }
    }
}
