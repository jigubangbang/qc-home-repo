package com.jigubangbang.com_service.model.chat_service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupApplyNotificationRequestDto {
    
    private String creatorId;
    private String groupName; 
    private int groupId;       
    private String relatedUrl;
    private String applicantId;
    private String nickname;
}
