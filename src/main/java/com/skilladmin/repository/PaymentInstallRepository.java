package com.skilladmin.repository;

import com.skilladmin.model.PaidInstallments;
import com.skilladmin.model.PaymentInstallment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentInstallRepository extends JpaRepository<PaymentInstallment,Long> {
}
