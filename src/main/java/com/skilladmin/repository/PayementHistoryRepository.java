package com.skilladmin.repository;

import com.skilladmin.model.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PayementHistoryRepository extends JpaRepository<PaymentHistory,Long> {

	
	@Query("SELECT COALESCE(SUM(p.paidAmount), 0) FROM PaymentHistory p WHERE YEAR(p.paymentDate) = :year AND MONTH(p.paymentDate) = :month")
	Long getTotalPaidAmountByMonth(@Param("year") int year, @Param("month") int month);
}
