package com.skilladmin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.skilladmin.model.Questions;

public interface QuestionRepository  extends JpaRepository<Questions, Long> {
	
	
    List<Questions> findByAssessmentTestTesId(Long testId);

    @Query("select q, a.testName from Questions q left join  AssessmentTest a on q.examId=a.tesId")
    List<Object[]> getQuestions();

}
