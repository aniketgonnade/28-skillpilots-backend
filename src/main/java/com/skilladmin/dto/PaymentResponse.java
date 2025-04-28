package com.skilladmin.dto;

import lombok.Data;

@Data
public class PaymentResponse {


    private String status;
    private String message;


    public PaymentResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
