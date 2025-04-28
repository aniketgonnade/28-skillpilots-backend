package com.skilladmin.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class CampusResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resultId;
    private int marks;

    private int correctAns;

    private int wrongAns;
    private int noAns;
    private int outof;
    private int totalMarks;
    private Long testId;
    private Long cuttoff = 20L;
    private String resultStatus;
    private Long studentId;
}
