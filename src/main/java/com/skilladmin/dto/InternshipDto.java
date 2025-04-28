package com.skilladmin.dto;

import java.util.Date;

import lombok.Data;

@Data
public class InternshipDto {
	private Long studentId;
    private Long companyId;
    private Long collegeId;
    private String companyName;
    private String technology;
    private String studentName;
    private int duration;
    private Date dateStarted;
    private Date dateCompleted;
    private String downloadUrl;








}
