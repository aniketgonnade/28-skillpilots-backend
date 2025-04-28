package com.skilladmin.controller;

import com.skilladmin.dto.PaidInstallmentResponseDTO;
import com.skilladmin.model.PaidInstallments;
import com.skilladmin.model.PaymentInstallment;
import com.skilladmin.model.TraineePaymentDetails;
import com.skilladmin.service.PaidInstallmentsService;
import com.skilladmin.service.PaymentInstallmentService;
import com.skilladmin.service.TrainePaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"*"})
public class PaidInstallmentsController {
    @Autowired
    private PaidInstallmentsService paidInstallmentsService;
    @Autowired
    private PaymentInstallmentService paymentInstallmentService;
    @Autowired
    private TrainePaymentService trainePaymentService;


    @PostMapping("/savePaid")
    public ResponseEntity<String> savePayment(@RequestBody PaidInstallments payment) {
        try {
            paidInstallmentsService.createPaid(payment);
            return ResponseEntity.ok("Payment saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error saving payment: " + e.getMessage());
        }
    }

    //
    @PostMapping("/saveIns")
    public ResponseEntity<Object> createInst(@RequestBody PaymentInstallment paymentInstallment) {
        HashMap<Object, Object> response = new HashMap<>();
        try {
            PaymentInstallment inst = paymentInstallmentService.createInst(paymentInstallment);
            response.put("msg", "Installment added successfully");
            response.put("installments", inst);
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>("Payment Failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAllIns")
    public ResponseEntity<Object> getAllIns() {
        Map<Object, Object> response = new HashMap<>();

        List<PaymentInstallment> paymentInstallment = paymentInstallmentService.findAll();
        if (!paymentInstallment.isEmpty()) {
            response.put("msg", "List of Divided Installments from total fees");
            response.put("installments", paymentInstallment);
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);

        } else {
            response.put("msg", "No daata found");
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);

        }
    }

    @GetMapping("/getAllPaidHistory")
    public ResponseEntity<Map<String, Object>> getAllPaid() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<PaidInstallmentResponseDTO> paidInstallments = paidInstallmentsService.findAllPaidInstallment();

            if (paidInstallments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            response.put("status", "success");
            response.put("data", paidInstallments);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Internal server error");

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    
    @GetMapping("/receipt")
    public ResponseEntity<PaidInstallments> getReceipt(@RequestParam Long rid) {
        PaidInstallments paidReceipt = paidInstallmentsService.findByRid(rid);

        // Check if the receipt is found
        if (paidReceipt == null) {
            return ResponseEntity.notFound().build(); 
        }

        return ResponseEntity.ok(paidReceipt); 
    }
    
    
    @GetMapping("/total-paid")
    public ResponseEntity<Object> getTotalPaidByStudentAndInstallment(
            @RequestParam(required = true) Long studentId,
            @RequestParam(required = true) Long instId) {

        try {

            if (studentId == null || instId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing required parameters: studentId or instId");
            }
            // Fetch total paid amount for the given student and installment
            Double totalPaid = paidInstallmentsService.getTotalPaidByStudentAndInstallment(studentId, instId);
            if (totalPaid == null || totalPaid == 0) {

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No payments found for student ID " + studentId + " and installment ID " + instId);
            }
            return ResponseEntity.ok(totalPaid);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/total-paid-by-student")
    public ResponseEntity<Object> getTotalPaidByStudent(@RequestParam Long studentId) {
        try {
            if (studentId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing required parameter: studentId");
            }
            Double totalPaid = paidInstallmentsService.getTotalPaidByStudent(studentId);
            if (totalPaid == null || totalPaid == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No payments found for student ID " + studentId);
            }
            return ResponseEntity.ok(totalPaid);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }

    }

    @GetMapping("/getDataById")
    public ResponseEntity<Object> getDataById(@RequestParam Long id, @RequestParam Long rid) {
        try {
            PaidInstallments paidInstallments = paidInstallmentsService.getById(id, rid);
            return ResponseEntity.ok(paidInstallments);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/createReceipt")
    public ResponseEntity<Object> createReceipt(@RequestParam Long studentId, @RequestParam("file") MultipartFile file) throws IOException {
        try {
            // Call the service method to save the file
            TraineePaymentDetails savedDetails = paidInstallmentsService.createReceipt(studentId, file);
            return ResponseEntity.status(HttpStatus.OK)
                                 .body(Map.of("msg", "File saved successfully", "details", savedDetails));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error: " + e.getMessage());
        }
    }


    @GetMapping("/receiptDetails")
    public  ResponseEntity<Object> getReceiptDetails(@RequestParam Long studentId){
     List<TraineePaymentDetails> paymentDetails=   trainePaymentService.getPaymentDeails(studentId);
        if (paymentDetails != null) {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("msg", "details add succesfully", "details", paymentDetails));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: ");
        }

    }

    @DeleteMapping("/deleteInstallment")
    public ResponseEntity<String> deleteInstallment(@RequestParam Long id) {
        try {
            paidInstallmentsService.deleteInstallMent(id);
            return ResponseEntity.ok("Installment deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete installment: " + e.getMessage());
        }
    }


}




