package com.skilladmin.service;

import com.skilladmin.model.PaymentInstallment;

import java.util.List;

public interface PaymentInstallmentService {
    PaymentInstallment createInst(PaymentInstallment paymentInstallment);
    List<PaymentInstallment> findAll();
}
