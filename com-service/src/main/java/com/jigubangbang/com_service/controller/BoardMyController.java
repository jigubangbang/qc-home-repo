package com.jigubangbang.com_service.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jigubangbang.com_service.model.BoardListResponse;
import com.jigubangbang.com_service.service.BoardService;

@RequestMapping("/user-com/board")
@RestController
public class BoardMyController {
    @Autowired
    private BoardService boardService;

        
    @GetMapping("/my-posts")
    public ResponseEntity<BoardListResponse> getMyPosts(
            @RequestHeader("User-Id") String userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(defaultValue = "5") int size) {
        
        BoardListResponse response = boardService.getMyPosts(userId, page, sort, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-comments")
    public ResponseEntity<Map<String, Object>> getMyCommentedPosts(
            @RequestHeader("User-Id") String userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(defaultValue = "5") int size) {
        
        Map<String, Object> response = boardService.getMyCommentedPosts(userId, page, sort, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-bookmarks")
    public ResponseEntity<Map<String, Object>> getBookmarkedPosts(
            @RequestHeader("User-Id") String userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(defaultValue = "5") int size) {
        
        Map<String, Object> response = boardService.getBookmarkedPosts(userId, page, sort, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-likes")
    public ResponseEntity<Map<String, Object>> getLikedPosts(
            @RequestHeader("User-Id") String userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(defaultValue = "5") int size) {
        
        Map<String, Object> response = boardService.getLikedPosts(userId, page, sort, size);
        return ResponseEntity.ok(response);
    }
}
