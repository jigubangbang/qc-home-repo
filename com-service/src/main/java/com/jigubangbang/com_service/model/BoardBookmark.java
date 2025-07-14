package com.jigubangbang.com_service.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardBookmark {
    private int id;
    private String userId;
    private int postId;
    private LocalDateTime createdAt;
}
