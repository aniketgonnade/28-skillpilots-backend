package com.skilladmin.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@Entity
public class PaymentStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long collegeId;
    private String status;
    private String createdAt;

    @PrePersist
    public void createdAt(){

        DateTimeFormatter dateTimeFormatter= DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.createdAt= LocalDate.now().format(dateTimeFormatter);
    }

}
