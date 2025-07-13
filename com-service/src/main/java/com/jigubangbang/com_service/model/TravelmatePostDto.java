package com.jigubangbang.com_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TravelmatePostDto {
    private Long id;
    private String creatorId;
    private String title;
    private String status;
}
