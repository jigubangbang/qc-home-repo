package com.jigubangbang.com_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeStatusResponse {
    private boolean isLiked;
    private int likeCount;
}

