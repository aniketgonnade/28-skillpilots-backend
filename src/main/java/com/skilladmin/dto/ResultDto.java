package com.skilladmin.dto;

import lombok.Data;

@Data
public class ResultDto {

	private Long resultId;
	private int marks;

	private int correctAns;

	private int wrongAns;
	private int noAns;
	private int outof;
	private int totalMarks;

	private String resultStatus;

	private String testName;
}
