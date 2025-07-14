package com.jigubangbang.com_service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReportRequest {
    private String targetUserId;
    private String contentSubtype;
    private String contentType;
    private Long contentId;
    private String reasonCode;
    private String reasonText;
}
