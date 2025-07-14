package com.jigubangbang.com_service.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateBoardRequest {
    private String title;
    private String content;
    private Integer boardId;
    private List<String> images;
}
