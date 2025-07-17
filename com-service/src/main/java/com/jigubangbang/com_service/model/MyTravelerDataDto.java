package com.jigubangbang.com_service.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyTravelerDataDto {
    private List<MyHostedTravelmateDto> hostedTravelmates;
    private List<MyJoinedTravelmateDto> joinedTravelmates;
    private List<MyAppliedTravelmateDto> appliedTravelmates;
    private List<MyLikedTravelmateDto> likedTravelmates;
    private List<MyCompletedTravelmateDto> completedTravelmates;
    private List<MyJoinedTravelInfoDto> joinedTravelInfos;
    private List<MyLikedTravelInfoDto> likedTravelInfos;
    private List<MyHostedTravelInfoDto> hostedTravelInfos;
}
