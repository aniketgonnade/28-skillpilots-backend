package com.skilladmin.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RecruitmentDTO {
    private String companyName;
    private Long contactNo;
    private String email;
    private String address;
    private String profile;
    private String departmentIds;

    //private RecruitmentDetailsDTO placementDetails;
    private String jobTitle;
    private String jobDescription;
    private String isOpen;
    private LocalDate interviewStartDate;
    private LocalDate interviewEndDate;
    private String criteria;
}
