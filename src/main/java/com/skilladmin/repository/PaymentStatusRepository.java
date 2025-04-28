package com.skilladmin.repository;

import com.skilladmin.model.PaymentStatus;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentStatusRepository extends JpaRepository<PaymentStatus,Long> {


    public PaymentStatus  findByCollegeId(Long collegeId);
    
    @Query(value = "SELECT p.history_id AS historyId, " +
            "u.username AS username, " +
            "p.paid_amount AS paidAmount, " +
            "p.payment_type AS paymentType, " +
            "p.payment_date AS paymentDate, " +
            "p.status AS status, " +
            "p.receipt_number AS receiptNumber " +
            "FROM payment_history p " +
            "LEFT JOIN user u ON p.user_id = u.id " +
            "ORDER BY p.payment_date DESC", nativeQuery = true)
List<Map<String, Object>> findByUserIdWithUsername();
}
