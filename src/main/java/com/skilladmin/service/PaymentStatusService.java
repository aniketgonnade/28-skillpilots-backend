package com.skilladmin.service;

import java.util.List;
import java.util.Map;

import com.skilladmin.model.PaymentStatus;

public interface PaymentStatusService
{
    public PaymentStatus addStatus(PaymentStatus paymentStatus);

    public PaymentStatus getPaymentStatus(Long collegeId);
    
    public List<Map<String, Object>> getPaymentHistory();
}
