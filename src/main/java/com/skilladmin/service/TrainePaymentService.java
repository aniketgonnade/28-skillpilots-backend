package com.skilladmin.service;

import com.skilladmin.model.TraineePaymentDetails;

import java.util.List;

public interface TrainePaymentService {

//    public TraineePaymentDetails addPaymentDetails(Long studentId,Long batchId, TraineePaymentDetails paymentDetails);

    public List<TraineePaymentDetails> getPaymentDeails(Long studentId);

}
