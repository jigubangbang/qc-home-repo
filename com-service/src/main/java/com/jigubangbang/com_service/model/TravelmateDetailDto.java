package com.jigubangbang.com_service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TravelmateDetailDto extends TravelmateDto {
    private String backgroundImage;
    private String description;
    private String applicationDescription;
    private String creatorId;
    private String creatorProfileImage;
}