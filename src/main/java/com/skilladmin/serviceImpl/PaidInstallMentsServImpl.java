package com.skilladmin.serviceImpl;

import com.skilladmin.dto.PaidInstallmentResponseDTO;
import com.skilladmin.model.Batch;
import com.skilladmin.model.PaidInstallments;
import com.skilladmin.model.TraineePaymentDetails;
import com.skilladmin.model.TrainingUsers;
import com.skilladmin.repository.BatchRepository;
import com.skilladmin.repository.PaidInstallmentsRe;
import com.skilladmin.repository.TrainePaymentRepository;
import com.skilladmin.repository.TrainingUserRepository;
import com.skilladmin.service.PaidInstallmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
public class PaidInstallMentsServImpl implements PaidInstallmentsService {
    @Autowired
    private PaidInstallmentsRe paidInstallmentsRe;
    @Autowired
    private TrainingUserRepository trainingUserRepository;
    @Autowired
    private BatchRepository batchRepository;


    @Autowired
    private TrainePaymentRepository paymentRepository;

    @Override
    public PaidInstallments createPaid(PaidInstallments paidInstallments) {
        return paidInstallmentsRe.save(paidInstallments);
    }

    @Override
    public List<PaidInstallmentResponseDTO> findAllPaidInstallment() {
        List<PaidInstallments> paidInstallments = paidInstallmentsRe.findAll();
        List<PaidInstallmentResponseDTO> responseDTOs = new ArrayList<>();
        
        for (PaidInstallments installment : paidInstallments) {
            // Convert studentId to Long if it's a String
            Long studentId = Long.valueOf(installment.getStudentId()); // Ensure this conversion is correct
            
            // Fetch the corresponding TrainingUsers entity by studentId
            TrainingUsers user = trainingUserRepository.findById(studentId)
                    .orElse(null);

            // If user is found, map the installment data and user name to DTO
            if (user != null) {
                PaidInstallmentResponseDTO dto = new PaidInstallmentResponseDTO();
                dto.setId(installment.getId());
                dto.setInstId(installment.getInstId());
                dto.setMode(installment.getMode());
                dto.setBalance(installment.getBalance());
                dto.setStudentId(studentId);
                dto.setPaid(installment.getPaid());
                dto.setStudentName(user.getName()); // Map the student name from TrainingUsers
                dto.setBatchName(installment.getBatchName()); // Assuming batch name is available in installment
//                dto.setStudentId(installment.getStudentId());
//                dto.setBatchId(installment.getBatchId()); // Ensure batchId is set if available
                dto.setRid(installment.getRid());
                responseDTOs.add(dto); // Add the DTO to the response list
            }
        }
        return responseDTOs; // Return the list of DTOs with installment and student info
    }


    @Override
    public List<PaidInstallments> findAll() {
        return paidInstallmentsRe.findAll();
    }

    @Override
    public Double getTotalPaidByStudentAndInstallment(Long studentId, Long instId) {
        return paidInstallmentsRe.findTotalPaidByStudentAndInstallment(studentId,instId);
    }

    @Override
    public Double getTotalPaidByStudent(Long studentId) {
        return paidInstallmentsRe.findTotalPaidByStudentId(studentId);
    }

    @Override
    public PaidInstallments getById(Long id,Long rid) {
        return paidInstallmentsRe.findByIdAndRid(id,rid);

    }



    @Override
    public void deleteInstallMent(Long id) {
         paidInstallmentsRe.deleteById(id);
    }

	@Override
	public Double totalPaidById(Long id) {
		// TODO Auto-generated method stub
		return paidInstallmentsRe.findTotalPaidByStudentId(id);
	}

	@Override
	public PaidInstallments findByRid(Long rid) {
		// TODO Auto-generated method stub
		return paidInstallmentsRe.findByRid(rid);
	}

	@Override
	public TraineePaymentDetails createReceipt(Long studentId, MultipartFile file) throws IOException {
	    // Create a new TraineePaymentDetails object with the file details
	    TraineePaymentDetails details = new TraineePaymentDetails();
	    details.setStudentId(studentId);
	    details.setReceiptUrl(file.getOriginalFilename());  // Store the filename or path as needed
	    details.setPdf(file.getBytes());  // Save the file bytes

	    // Save and return the saved entity
	    return paymentRepository.save(details);
	}





}



