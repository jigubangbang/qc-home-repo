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
public class ComNotificationRequestDto {

    private String authorId;
    private int postId;
    private String relatedUrl;
    private String senderId;
    private String senderProfileImage;
    private String nickname;
}