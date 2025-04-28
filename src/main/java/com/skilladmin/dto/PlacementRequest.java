package com.skilladmin.dto;

import com.skilladmin.model.Recruitment;
import com.skilladmin.model.RecruitmentDetails;

import lombok.Data;

import java.util.List;

@Data
public class PlacementRequest {

    private Recruitment placement;
    private RecruitmentDetails placementDetails;

   String departmentIds;
}
