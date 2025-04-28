package com.skilladmin.serviceImpl;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skilladmin.model.AssessmentTest;
import com.skilladmin.repository.ExamRepository;
import com.skilladmin.service.ExamService;
import com.skilladmin.util.ProjectConstants;
@Service
public class ExamServiceImpl implements ExamService{

	@Autowired
	private ExamRepository examRepository;
	
	
	@Override
	public AssessmentTest createExam(AssessmentTest assessmentTest) {
		
		assessmentTest.setCreation_date(new Date());
	//	assessmentTest.setStatus(ProjectConstants.exam_status_inactive);
		examRepository.save(assessmentTest);
		return assessmentTest;
	}


	@Override
	public AssessmentTest getExmById(Long testId) {
		return examRepository.findById(testId).orElseThrow(()-> new RuntimeException("Test Not found"+testId));
	}


	@Override
	public List<AssessmentTest> getAllTest(){
		return examRepository.findAll();
	}


	@Override
	public AssessmentTest edit(AssessmentTest assessmentTest) {
		// TODO Auto-gnerated method stub
		return examRepository.save(assessmentTest);
	}
	
	@Override
	 public boolean activateTest(Long testId) {
		 AssessmentTest test = examRepository.findById(testId).orElseThrow(()-> new RuntimeException("Not found assesment"));
        test.setStatus(!test.isStatus());

	        examRepository.save(test);
		return test.isStatus();

	}


	@Override
	public void deleteTest(Long testId) {
		examRepository.deleteById(testId);
	}}
