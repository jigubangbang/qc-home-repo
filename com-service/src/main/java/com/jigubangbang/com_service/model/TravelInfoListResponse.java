package com.jigubangbang.com_service.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TravelInfoListResponse {
    private List<TravelInfoDto> travelInfos;
    private int totalCount;

    public TravelInfoListResponse() {}

    public TravelInfoListResponse(List<TravelInfoDto> travelInfos, int totalCount) {
        this.travelInfos = travelInfos;
        this.totalCount = totalCount;
    }

}