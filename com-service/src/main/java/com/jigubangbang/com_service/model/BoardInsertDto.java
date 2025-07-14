package com.jigubangbang.com_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardInsertDto {
    private Integer id;
    private String userId;
    private Integer boardId;
    private String title;
    private String content;
}
