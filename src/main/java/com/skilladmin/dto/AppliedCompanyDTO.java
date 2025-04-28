package com.skilladmin.dto;

import lombok.Data;

@Data
public class AppliedCompanyDTO {
	   public AppliedCompanyDTO(String companyName, String jobRole, String appliedDate, String status) {
	        this.companyName = companyName;
	        this.jobRole = jobRole;
	        this.appliedDate = appliedDate;
	        this.status = status;
	    }
	private String companyName;
   private String jobRole;
   private String appliedDate;
   private String status;
}
