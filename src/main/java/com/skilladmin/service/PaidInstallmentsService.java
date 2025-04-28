package com.skilladmin.service;

import com.skilladmin.dto.PaidInstallmentResponseDTO;
import com.skilladmin.model.PaidInstallments;
import com.skilladmin.model.TraineePaymentDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PaidInstallmentsService {
    PaidInstallments createPaid(PaidInstallments paidInstallments) ;
    List<PaidInstallmentResponseDTO> findAllPaidInstallment();
    List<PaidInstallments> findAll();
    Double getTotalPaidByStudentAndInstallment(Long studentId, Long instId);  // shivani

    Double getTotalPaidByStudent(Long studentId);

    PaidInstallments getById(Long id,Long rid);

  

    void deleteInstallMent(Long id);
    
	Double totalPaidById(Long id);
	PaidInstallments findByRid(Long rid);
	TraineePaymentDetails createReceipt(Long studentId, MultipartFile file) throws IOException;

}
