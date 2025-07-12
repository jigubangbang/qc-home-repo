package com.jigubangbang.com_service.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelmateApplicationDto {
    private Integer id;
    private Long mateId;
    private String userId;
    private String userNickname;
    private String applicationComment;
    private String status;
    private LocalDateTime appliedAt;
    private LocalDateTime respondedAt;
}
