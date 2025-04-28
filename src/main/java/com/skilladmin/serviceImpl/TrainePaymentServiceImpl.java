package com.skilladmin.serviceImpl;

import com.skilladmin.model.Batch;
import com.skilladmin.model.PaymentHistory;
import com.skilladmin.model.TraineePaymentDetails;
import com.skilladmin.repository.BatchRepository;
import com.skilladmin.repository.PayementHistoryRepository;
import com.skilladmin.repository.TrainePaymentRepository;
import com.skilladmin.service.TrainePaymentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TrainePaymentServiceImpl implements TrainePaymentService {
    @Autowired
    private TrainePaymentRepository paymentRepository;
    @Autowired
    private PayementHistoryRepository payementHistoryRepository;
    @Autowired
    private BatchRepository batchRepository;

//    @Override
//    public TraineePaymentDetails addPaymentDetails(Long studentId, Long batchId, TraineePaymentDetails paymentDetails) {
//        TraineePaymentDetails traineePaymentDetails = new TraineePaymentDetails();
//
//        // Fetch existing payment details for the student
//        TraineePaymentDetails existingDetails = paymentRepository.findByStudentId(studentId);
//
//        if (existingDetails == null) {
//            // No previous payment record exists, create new
//            Batch batch = batchRepository.findById(batchId).orElseThrow(() -> new RuntimeException("Batch not found"));
//
//            // Apply discount if provided
//            if (paymentDetails.getDiscountAmount() > 0) {
//                traineePaymentDetails.setDiscountAmount(paymentDetails.getDiscountAmount());
//            }
//
//            // Set initial payment details
//            traineePaymentDetails.setPaidAmount(paymentDetails.getPaidAmount());
//            traineePaymentDetails.setDueAmount(batch.getPrice() - paymentDetails.getPaidAmount() - paymentDetails.getDiscountAmount());
//            traineePaymentDetails.setPaymentType(paymentDetails.getPaymentType());
//            traineePaymentDetails.setLatestPaymentDate(LocalDate.now());
//            traineePaymentDetails.setStudentId(studentId);
//
//            // Save the new payment record
//            paymentRepository.save(traineePaymentDetails);
//
//            // Create payment history for the new payment
//            PaymentHistory history = new PaymentHistory();
//            history.setTraineePaymentDetails(traineePaymentDetails);
//            history.setPaidAmount(paymentDetails.getPaidAmount());
//            history.setPaymentType(paymentDetails.getPaymentType());
//            history.setPaymentDate(LocalDate.now());
//            payementHistoryRepository.save(history);
//
//            return traineePaymentDetails;
//
//        } else {
//            // Update existing payment record
//            // Check if due amount is zero, no further payments required
//            if (existingDetails.getDueAmount() == 0) {
//                throw new RuntimeException("No due amount remaining. Full payment has already been made.");
//            }
//
//            // Apply discount if provided
//            if (paymentDetails.getDiscountAmount() > 0) {
//                existingDetails.setDiscountAmount(paymentDetails.getDiscountAmount());
//            }
//
//            // Update the total paid amount and the due amount
//            double newPaidAmount = existingDetails.getPaidAmount() + paymentDetails.getPaidAmount();
//            existingDetails.setPaidAmount(newPaidAmount);
//            existingDetails.setPaymentType(paymentDetails.getPaymentType());
//            existingDetails.setLatestPaymentDate(LocalDate.now());
//            existingDetails.setDueAmount(existingDetails.getDueAmount() - paymentDetails.getPaidAmount());
//
//            // Save the updated payment details
//            paymentRepository.save(existingDetails);
//
//            // Create payment history for the updated payment
//            PaymentHistory history = new PaymentHistory();
//            history.setTraineePaymentDetails(existingDetails);  // Associate with existing details
//            history.setPaidAmount(paymentDetails.getPaidAmount());
//            history.setPaymentType(paymentDetails.getPaymentType());
//            history.setPaymentDate(LocalDate.now());
//            payementHistoryRepository.save(history);
//
//            return existingDetails;
//        }
//    }

    @Override
    public List<TraineePaymentDetails> getPaymentDeails(Long studentId) {

        return paymentRepository.findByStudentId(studentId);
    }

}
