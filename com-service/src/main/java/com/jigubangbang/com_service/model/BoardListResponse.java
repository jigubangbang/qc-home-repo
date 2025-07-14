package com.jigubangbang.com_service.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardListResponse {
    private List<BoardDto> posts;
    private int currentPage;
    private int totalPages;
    private int totalPosts;
    private boolean hasNext;
    private boolean hasPrevious;
}
