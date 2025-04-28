package com.skilladmin.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skilladmin.model.AssessmentTest;
import com.skilladmin.repository.AssessmentTestRepository;
import com.skilladmin.service.AssesmentService;

@Service
public class AssesmentServiceImpl implements AssesmentService {

	@Autowired
	private AssessmentTestRepository assessmentTestRepository;
	
	@Override
	public List<AssessmentTest> findAsssesments() {
		return assessmentTestRepository.findAll();
	}

	@Override
	public AssessmentTest findTests(Long testId) {
		return  assessmentTestRepository.findById(testId).orElseThrow();
	}

	@Override
	public AssessmentTest findByTestId(Long testId) {
		return assessmentTestRepository.findById(testId).orElseThrow(()-> new RuntimeException("Test not found"));
	}

}
