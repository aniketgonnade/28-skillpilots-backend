package com.skilladmin.dto;

import lombok.Data;

@Data
public class RecruitmentWithStatusDTO {


    private Long placementId;
    private String companyName;
    private String profile;
    private String ctc;
    private String departmentIds;
    private String status;    // Applied, Not Applied, etc.
    private String applyDate; // Optional: Date when applied
    private String round;
}
