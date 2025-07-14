package com.jigubangbang.com_service.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelmateSearchCriteria {
    private int pageNum;
    private int pageSize;
    private int offset;
    private List<String> locations;    // ["ALB-34", "KOR-1"] 형태
    private List<Integer> targets;     // [2, 3, 4] 형태 (theme_id)
    private List<Integer> themes;      // [12, 13] 형태 (theme_id)
    private List<String> styles;       // ["C", "D"] 형태 (style_id)
    private String sortOption;         // "default", "latest", "period", "likes"
    private boolean showCompleted;     // 완료된 모임 포함 여부
    private String search;
}
