package com.jigubangbang.com_service.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jigubangbang.com_service.model.BoardCommentDto;
import com.jigubangbang.com_service.model.BoardDto;
import com.jigubangbang.com_service.model.BoardListResponse;
import com.jigubangbang.com_service.model.CreateCommentRequest;
import com.jigubangbang.com_service.service.BoardService;


@RestController
@RequestMapping("/com/board")
public class BoardMainController {
    @Autowired
    private BoardService boardService;
    
    //조회
    @GetMapping("/list")
    public ResponseEntity<BoardListResponse> getBoardList(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(required = false) String category, // String으로 변경하여 배열 처리
            @RequestParam(defaultValue = "default") String sortOption,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String travelStyleId) {

        BoardListResponse response = boardService.getBoardList(pageNum, category, sortOption, search, limit, travelStyleId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Map<String, Object>> getBoardPost(@PathVariable("postId") int postId) {
        try {
            BoardDto post = boardService.getBoardPost(postId);
            return ResponseEntity.ok(Map.of("success", true, "data", post));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    
    @GetMapping("/{postId}/images")
    public ResponseEntity<List<String>> getBoardImages(@PathVariable Integer postId) {
        try {
            List<String> images = boardService.getBoardImages(postId);
            return ResponseEntity.ok(images);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    // 게시글 정보만 조회 (조회수 증가 안함) - 목록/미리보기용
    @GetMapping("/{postId}/info")
    public ResponseEntity<Map<String, Object>> getBoardPostInfo(@PathVariable("postId") int postId) {
        try {
            BoardDto post = boardService.getBoardPostInfo(postId);
            return ResponseEntity.ok(Map.of("success", true, "data", post));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", e.getMessage()));
        }
    }

    // 댓글 조회
    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<BoardCommentDto>> getBoardComments(@PathVariable Integer postId) {
        try {
            List<BoardCommentDto> comments = boardService.getBoardComments(postId);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    

}
