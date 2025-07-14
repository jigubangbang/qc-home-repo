package com.jigubangbang.com_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    private Long reportId;
    private String reporterId;
    private String targetUserId;
    private String contentSubtype;
    private String contentType;
    private Long contentId;
    private String reasonCode;
    private String reasonText;
    private String reportStatus;
}