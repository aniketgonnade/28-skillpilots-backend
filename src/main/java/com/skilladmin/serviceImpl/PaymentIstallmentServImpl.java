package com.skilladmin.serviceImpl;

import com.skilladmin.model.PaymentInstallment;
import com.skilladmin.repository.PaymentInstallRepository;
import com.skilladmin.service.PaymentInstallmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentIstallmentServImpl implements PaymentInstallmentService {

    @Autowired
    private PaymentInstallRepository paymentInstallRepository;

    @Override
    public PaymentInstallment createInst(PaymentInstallment paymentInstallment) {
        return paymentInstallRepository.save(paymentInstallment);
    }

    @Override
    public List<PaymentInstallment> findAll() {
        return paymentInstallRepository.findAll();
    }
}
