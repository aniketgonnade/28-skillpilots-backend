package com.skilladmin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long historyId;
    private double paidAmount;
    private String paymentType;  // e.g., "CASH", "ONLINE"
    @CurrentTimestamp
    private LocalDate paymentDate;
    private String status;
    private Long userId;
    private String receiptNumber;

}
