package com.skilladmin.service;

import java.util.List;

import com.skilladmin.model.AssessmentTest;

public interface ExamService {
	
	
	public AssessmentTest createExam(AssessmentTest assessmentTest);

	public AssessmentTest getExmById(Long testId);
	
	public List<AssessmentTest> getAllTest();
	
	public AssessmentTest edit(AssessmentTest assessmentTest);
	public boolean activateTest(Long testId);

	public void deleteTest(Long testId);

}
