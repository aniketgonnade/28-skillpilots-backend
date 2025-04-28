package com.skilladmin.controller;

import com.skilladmin.model.PaymentStatus;
import com.skilladmin.service.PaymentStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(value = "*")
@RestController
public class PaymentStatusController {
    private final PaymentStatusService paymentStatusService;

    public PaymentStatusController(PaymentStatusService paymentStatusService) {
        this.paymentStatusService = paymentStatusService;
    }

// for add payment status from admin i.e. i college paying or student paying
    @PostMapping("/addPaymentStatus")
    public ResponseEntity<?> craetePaymentStatus(@RequestBody PaymentStatus paymentStatus)
    {

        try
        {
            return ResponseEntity.status(200).body(paymentStatusService.addStatus(paymentStatus));
        }
        catch (Exception e)
        {

            return ResponseEntity.status(500).body(Map.of("msg", e.getMessage()));
        }
    }


    // check status who is paying
    @GetMapping("/getPaymentStatus")
    public ResponseEntity<?> getPaymentId(@RequestParam("collegeId") long collegeId)
    {
        PaymentStatus paymentStatus = paymentStatusService.getPaymentStatus(collegeId);
        if(paymentStatus != null)
        {
            return ResponseEntity.status(200).body(paymentStatus);
        }
        return ResponseEntity.status(500).body(Map.of("msg","Not Found"));
    }
    
    @GetMapping("/getAllHistory")
    public List<Map<String, Object>> getPaymentHistoryDetails()
    {
    	return paymentStatusService.getPaymentHistory();
    }
}
