package com.skilladmin.service;

import java.util.List;

import com.skilladmin.model.AssessmentTest;

public interface AssesmentService {

	List<AssessmentTest> findAsssesments();

	AssessmentTest findTests(Long testId);
	public AssessmentTest findByTestId(Long testId);


}
