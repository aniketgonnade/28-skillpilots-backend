package com.skilladmin.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RecruitmentDetailsDTO {
    private String companyName;
    private String jobTitle;
    private String jobDescription;
    private String isOpen;
    private LocalDate interviewStartDate;
    private LocalDate interviewEndDate;
    private String criteria;
}
