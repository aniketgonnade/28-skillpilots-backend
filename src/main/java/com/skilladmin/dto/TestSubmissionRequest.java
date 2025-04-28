package com.skilladmin.dto;

import java.util.Map;

import lombok.Data;

@Data
public class TestSubmissionRequest {

	    private Long studentId;
	    private Long testId;
	    private Map<String, String> answers;
	    private boolean disqualified;
	
}
