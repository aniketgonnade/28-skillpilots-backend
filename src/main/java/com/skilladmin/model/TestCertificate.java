package com.skilladmin.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class TestCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long testId;
    private int score;
    private LocalDateTime issuedAt;

    private String level; // EASY, MEDIUM, HARD


    private String certificateUrl;

    @OneToOne
    private UserTestResult userTestResult;
}
