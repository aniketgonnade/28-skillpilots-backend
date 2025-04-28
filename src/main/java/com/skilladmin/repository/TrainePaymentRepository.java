package com.skilladmin.repository;

import com.skilladmin.model.TraineePaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainePaymentRepository extends JpaRepository<TraineePaymentDetails,Long> {

    public List<TraineePaymentDetails> findByStudentId(Long studentId);

}
