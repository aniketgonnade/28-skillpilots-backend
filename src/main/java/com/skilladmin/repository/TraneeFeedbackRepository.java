package com.skilladmin.repository;

import com.skilladmin.model.TraineeFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TraneeFeedbackRepository extends JpaRepository<TraineeFeedback,Long> {

    public List<TraineeFeedback> findByStudentId(Long studentId);
}
