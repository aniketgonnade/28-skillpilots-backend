package com.skilladmin.dto;

import lombok.Data;

@Data
public class PaymentRequest {

    private String name;
    private String mobile;
    private String productName;
    private String url;
    private double amount;
}
