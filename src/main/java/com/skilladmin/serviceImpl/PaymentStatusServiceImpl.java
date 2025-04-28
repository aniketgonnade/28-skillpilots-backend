package com.skilladmin.serviceImpl;

import com.skilladmin.model.PaymentStatus;
import com.skilladmin.repository.PaymentStatusRepository;
import com.skilladmin.service.PaymentStatusService;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class PaymentStatusServiceImpl implements PaymentStatusService
{

    private final PaymentStatusRepository paymentStatusRepository;

    public PaymentStatusServiceImpl(PaymentStatusRepository paymentStatusRepository) {
        this.paymentStatusRepository = paymentStatusRepository;
    }

    @Override
    public PaymentStatus addStatus(PaymentStatus paymentStatus) {
        PaymentStatus existingPayment = paymentStatusRepository.findByCollegeId(paymentStatus.getCollegeId());
        
        if (existingPayment != null) {
            // Update only the status if the record exists
            existingPayment.setStatus(paymentStatus.getStatus());
            return paymentStatusRepository.save(existingPayment);
        } else {
            // Insert a new record if no existing record is found
            return paymentStatusRepository.save(paymentStatus);
        }
    }


    @Override
    public PaymentStatus getPaymentStatus(Long collegeId) {
        return paymentStatusRepository.findByCollegeId(collegeId);
    }

	@Override
	public List<Map<String, Object>> getPaymentHistory() {
		// TODO Auto-generated method stub
		return paymentStatusRepository.findByUserIdWithUsername();
	}


}
