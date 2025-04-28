package com.skilladmin.dto;

import lombok.Data;

@Data
public class PaidInstallmentResponseDTO {

    private Long Id;
    private Double paid;
    private String instId;
    private Long studentId;
    private Long batchId;
    private String studentName;
    private String batchName;
    private Long rid;

    private String mode;

    private Double batchFees;

   
    private Double balance; // Updated to include balance


    
}
