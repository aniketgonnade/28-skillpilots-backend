package com.skilladmin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.skilladmin.model.Results;

public interface ResultRepository extends JpaRepository<Results, Long> {

  @Query("select r.marks,r.resultStatus,a.testName,r.correctAns,r.outof from Results r join AssessmentTest a on r.testId=a.tesId where r.studentId=:studentId ") 
  List<Object[]> getTestHistory(Long studentId);


}
