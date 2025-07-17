package com.jigubangbang.com_service.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BoardCommentDto {
    private Integer id;
    private String userId;
    private String nickname;
    private String profileImage;
    private Integer postId;
    private String content;
    private Integer level;
    private Integer parentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String blindStatus;
    private Boolean isDeleted;
    private List<BoardCommentDto> replies;

    public BoardCommentDto() {
        this.replies = new ArrayList<>();
    }
}
