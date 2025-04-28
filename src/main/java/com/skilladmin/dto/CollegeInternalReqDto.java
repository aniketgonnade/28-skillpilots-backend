package com.skilladmin.dto;

import java.util.Date;


import lombok.Data;

@Data
public class CollegeInternalReqDto {

	 private String companyName;
	    private Long userId;
	    private String requestMsg;
	    private String rejectionMsg;
	    private String technology;
	    private int duration;
	    private String approvalStatus;
	    private Long againstExtReq;
	    private String forUsers;
	    private Date creationDate;
	    private Date updationDate;
	    private Date expirationDate;
	    private Long requestId;
	    private Long testDataId;
	    private Date testDataExpirationDate;
	    private String testDataStatus;
	 
}