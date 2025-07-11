package com.jigubangbang.com_service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TravelmateDetailResponse {
    private TravelmateDetailDto travelmate;
    
    public TravelmateDetailResponse(TravelmateDetailDto travelmate) {
        this.travelmate = travelmate;
    }
}
