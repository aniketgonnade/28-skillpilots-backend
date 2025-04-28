package com.skilladmin.controller;

import com.skilladmin.model.TraineePaymentDetails;
import com.skilladmin.service.TrainePaymentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PaymenDetailsController {
    @Autowired
    private TrainePaymentService trainePaymentService;

//    @PostMapping("/addPayment")
//    public ResponseEntity<Object> addPayment(@RequestParam Long studentId, @RequestParam Long batchId,
//                                             @RequestBody TraineePaymentDetails paymentDetails) {
//        try {
//            // Call the service to add payment details
//            TraineePaymentDetails details = trainePaymentService.addPaymentDetails(studentId, batchId, paymentDetails);
//
//            // Check if the due amount is now 0 after this payment
//            if (details.getDueAmount() <= 0) {
//                // If all dues are cleared, return a success response
//                return ResponseEntity.status(HttpStatus.OK).body(Map.of("msg","Payment successful. All dues are cleared."));
//            } else {
//                // Otherwise, return the updated payment details and remaining amount
//                return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("msg","Payment successful. Remaining due amount: " + details.getDueAmount()) );
//            }
//
//        } catch (EntityNotFoundException e) {
//            // Return not found response if the student or batch is not found
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("msg","Student or Batch not found."));
//        } catch (IllegalArgumentException e) {
//            // Handle case when invalid input is passed
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("msg","Invalid payment details provided."));
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("msg","No due amount remaining. Full payment has already been made."));
//        }
//        catch (Exception e) {
//            // Handle any other unexpected exceptions
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("msg","An error occurred while adding payment."));
//        }
//    }
}
