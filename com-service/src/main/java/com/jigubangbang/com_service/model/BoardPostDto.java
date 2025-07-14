package com.jigubangbang.com_service.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BoardPostDto {
    private Integer id;
    private String userId;
    private String title;
    private String content;
    private Integer boardId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
