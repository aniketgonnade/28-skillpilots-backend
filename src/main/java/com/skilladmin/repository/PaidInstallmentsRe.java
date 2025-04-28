package com.skilladmin.repository;

import com.skilladmin.model.PaidInstallments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaidInstallmentsRe extends JpaRepository<PaidInstallments,Long> {
    @Query("SELECT SUM(p.paid) FROM PaidInstallments p WHERE p.studentId = :studentId AND p.instId = :instId")
    Double findTotalPaidByStudentAndInstallment(@Param("studentId") Long studentId, @Param("instId") Long instId);
    @Query("SELECT SUM(p.paid) FROM PaidInstallments p WHERE p.studentId = :studentId")
    Double findTotalPaidByStudentId(@Param("studentId") Long studentId);

    public PaidInstallments findByIdAndRid(Long id,Long rid);
	PaidInstallments findByRid(Long rid);
}


