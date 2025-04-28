package com.skilladmin.service;

import com.phonepe.sdk.pg.common.http.PhonePeException;
import com.skilladmin.dto.PaymentRequest;
import com.skilladmin.dto.PaymentResponse;

public interface PhonePayService {
//    public PaymentResponse initiatePayment(String transactionId, Double amount) throws PhonePeException;
    public Object initiatePayment(PaymentRequest paymentRequest);
    public Object checkPaymentStatus(String txnId);

    }
