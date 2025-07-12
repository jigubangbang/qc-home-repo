package com.jigubangbang.com_service.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TravelmateCommentDto {
    private Long id;
    private String userId;
    private String nickname;
    private String profileImage;
    private Long mateId;
    private String content;
    private Integer level;
    private Long parentId;
    private String createdAt;
    private String updatedAt;
    private String blindStatus;
    private Boolean isDeleted;
    private List<TravelmateCommentDto> replies;
}
