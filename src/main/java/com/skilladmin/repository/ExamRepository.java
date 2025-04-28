package com.skilladmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skilladmin.model.AssessmentTest;

public interface ExamRepository extends JpaRepository<AssessmentTest, Long> {

}
