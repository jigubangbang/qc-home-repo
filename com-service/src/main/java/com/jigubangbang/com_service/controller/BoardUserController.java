package com.jigubangbang.com_service.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jigubangbang.com_service.chat_service.NotificationServiceClient;
import com.jigubangbang.com_service.model.BoardCreateResponse;
import com.jigubangbang.com_service.model.BookmarkResponse;
import com.jigubangbang.com_service.model.BookmarkStatusResponse;
import com.jigubangbang.com_service.model.CreateBoardRequest;
import com.jigubangbang.com_service.model.CreateCommentRequest;
import com.jigubangbang.com_service.model.LikeResponse;
import com.jigubangbang.com_service.model.LikeStatusResponse;
import com.jigubangbang.com_service.model.UpdateBoardRequest;
import com.jigubangbang.com_service.model.chat_service.ComNotificationRequestDto;
import com.jigubangbang.com_service.service.BoardService;

@RestController
@RequestMapping("/user-com/board")
public class BoardUserController {
    @Autowired
    private BoardService boardService;

    @Autowired
    private NotificationServiceClient notificationClient;

    //북마크
    @GetMapping("/bookmark/{postId}/status")
    public ResponseEntity<Map<String, Object>> getBookmarkStatus(
            @PathVariable("postId") int postId,
            @RequestHeader("User-Id") String userId) {
        
        try {
            BookmarkStatusResponse response = boardService.getBookmarkStatus(postId, userId);
            return ResponseEntity.ok(Map.of(
                "isBookmarked", response.isBookmarked(),
                "bookmarkCount", response.getBookmarkCount()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/bookmarks")
    public ResponseEntity<Map<String, Object>> getBookmarkedPosts(
            @RequestHeader("User-Id") String userId) {
        
        List<Integer> bookmarkedPostIds = boardService.getBookmarkedPostIds(userId);
        return ResponseEntity.ok(Map.of("bookmarkedPostIds", bookmarkedPostIds));
    }

    @PostMapping("/bookmark/{postId}")
    public ResponseEntity<Map<String, Object>> addBookmark(
            @PathVariable("postId") int postId,
            @RequestHeader("User-Id") String userId) {
        
        try {
            BookmarkResponse response = boardService.addBookmark(postId, userId);
            return ResponseEntity.ok(Map.of(
                "success", response.isSuccess(),
                "message", response.getMessage(),
                "bookmarkCount", response.getBookmarkCount()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @DeleteMapping("/bookmark/{postId}")
    public ResponseEntity<Map<String, Object>> removeBookmark(
            @PathVariable("postId") int postId,
            @RequestHeader("User-Id") String userId) {
        
        try {
            BookmarkResponse response = boardService.removeBookmark(postId, userId);
            return ResponseEntity.ok(Map.of(
                "success", response.isSuccess(),
                "message", response.getMessage(),
                "bookmarkCount", response.getBookmarkCount()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/like/{postId}/status")
    public ResponseEntity<Map<String, Object>> getLikeStatus(
            @PathVariable("postId") int postId,
            @RequestHeader("User-Id") String userId) {
        
        try {
            LikeStatusResponse response = boardService.getLikeStatus(postId, userId);
            return ResponseEntity.ok(Map.of(
                "isLiked", response.isLiked(),
                "likeCount", response.getLikeCount()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/likes")
    public ResponseEntity<Map<String, Object>> getLikedPosts(
            @RequestHeader("User-Id") String userId) {
        
        List<Integer> likedPostIds = boardService.getLikedPostIds(userId);
        return ResponseEntity.ok(Map.of("likedPostIds", likedPostIds));
    }
    
    @PostMapping("/like/{postId}")
    public ResponseEntity<Map<String, Object>> addLike(
            @PathVariable("postId") int postId,
            @RequestHeader("User-Id") String userId) {
        
        try {
            LikeResponse response = boardService.addLike(postId, userId);
            return ResponseEntity.ok(Map.of(
                "success", response.isSuccess(),
                "message", response.getMessage(),
                "likeCount", response.getLikeCount()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @DeleteMapping("/like/{postId}")
    public ResponseEntity<Map<String, Object>> removeLike(
            @PathVariable("postId") int postId,
            @RequestHeader("User-Id") String userId) {
        
        try {
            LikeResponse response = boardService.removeLike(postId, userId);
            return ResponseEntity.ok(Map.of(
                "success", response.isSuccess(),
                "message", response.getMessage(),
                "likeCount", response.getLikeCount()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 댓글 등록
    @PostMapping("/{postId}/comments")
    public ResponseEntity<Map<String, Object>> createBoardComment(
            @PathVariable Integer postId,
            @RequestBody CreateCommentRequest request,
            @RequestHeader("User-Id") String userId) {
        
        try {
        System.out.println("=== 댓글 작성 시작 ===");
        Map<String, Object> serviceResult = boardService.createBoardComment(postId, userId, request.getContent());
        
        // 알림 처리
        if (serviceResult.get("needNotification") != null && (Boolean) serviceResult.get("needNotification")) {
            System.out.println("댓글 알림 처리 시작");
            
            try {
                String postAuthorId = (String) serviceResult.get("postAuthorId");
                String postTitle = (String) serviceResult.get("postTitle");
                String commenterId = (String) serviceResult.get("commenterId");
                String nickname = (String) serviceResult.get("nickname");
                
                System.out.println("알림 데이터 - 받는이: " + postAuthorId + ", 게시글: " + postTitle);

                ComNotificationRequestDto notificationRequest = ComNotificationRequestDto.builder()
                    .authorId(postAuthorId)  // 게시글 작성자에게 알림
                    .nickname(nickname)
                    .postId(postId)
                    .relatedUrl("/board/" + postId)
                    .senderId(commenterId)
                    .senderProfileImage(null)
                    .build();
                
                ResponseEntity<Map<String, Object>> notificationResponse = 
                    notificationClient.createPostCommentNotification(notificationRequest);
                
                System.out.println("[BoardController] 댓글 알림 발송 성공: " + notificationResponse.getBody());
                
            } catch (Exception notificationError) {
                System.out.println("[BoardController] 댓글 알림 발송 실패: " + notificationError.getMessage());
                // 알림 실패해도 댓글 작성은 성공으로 처리
            }
        }
        
        return ResponseEntity.ok(Map.of("success", true, "message", "댓글이 등록되었습니다."));
        
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
}

    // 대댓글 등록
    @PostMapping("/{postId}/comments/{parentId}/replies")
    public ResponseEntity<Map<String, Object>> createBoardReply(
            @PathVariable Integer postId,
            @PathVariable Integer parentId,
            @RequestBody CreateCommentRequest request,
            @RequestHeader("User-Id") String userId) {
        
        try {
            boardService.createBoardReply(postId, parentId, userId, request.getContent());
            return ResponseEntity.ok(Map.of("success", true, "message", "답변이 등록되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 댓글 수정
    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Map<String, Object>> updateBoardComment(
            @PathVariable Integer postId,
            @PathVariable Integer commentId,
            @RequestBody CreateCommentRequest request,
            @RequestHeader("User-Id") String userId) {
        
        try {
            boardService.updateBoardComment(postId, commentId, userId, request.getContent());
            return ResponseEntity.ok(Map.of("success", true, "message", "댓글이 수정되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 댓글 삭제
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<Map<String, Object>> deleteBoardComment(
            @PathVariable Integer postId,
            @PathVariable Integer commentId,
            @RequestHeader("User-Id") String userId) {
        
        try {
            boardService.deleteBoardComment(postId, commentId, userId);
            return ResponseEntity.ok(Map.of("success", true, "message", "댓글이 삭제되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<BoardCreateResponse> createBoard(
            @RequestBody CreateBoardRequest request,
            @RequestHeader("User-Id") String userId) {
        
        try {
            BoardCreateResponse response = boardService.createBoard(userId, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                new BoardCreateResponse(null, e.getMessage())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new BoardCreateResponse(null, "게시글 작성에 실패했습니다.")
            );
        }
    }
    
    // 게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<Map<String, Object>> updateBoard(
            @PathVariable Integer postId,
            @RequestBody UpdateBoardRequest request,
            @RequestHeader("User-Id") String userId) {
        
        try {
            boardService.updateBoard(userId, postId, request);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "게시글이 성공적으로 수정되었습니다."
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "error", "게시글 수정에 실패했습니다."
            ));
        }
    }

    
    @DeleteMapping("/{postId}")
    public ResponseEntity<Map<String, Object>> deleteBoard(
            @PathVariable Integer postId,
            @RequestHeader("User-Id") String userId) {
        
        try {
            boardService.deleteBoard(userId, postId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "게시글이 성공적으로 삭제되었습니다."
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "success", false,
                "error", "게시글 삭제에 실패했습니다."
            ));
        }
    }
}

    