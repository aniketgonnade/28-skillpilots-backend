package com.skilladmin.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
@NoArgsConstructor
public class TraineePaymentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private Long studentId;

    @Column(length = 1000)
    private String receiptUrl;

    @Lob
    @Column(length = 1048576) // Adjust the length as needed
    private byte[] pdf;

    private String createdDate;

    @PrePersist
    public void create() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.createdDate= LocalDateTime.now().format(dateTimeFormatter);
    }

}
