package com.skilladmin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skilladmin.enumclass.QuestionLevel;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
public class UserTestResult {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "test_id")
    private Test test;

    private int score;
    private boolean passed;
    @Enumerated(EnumType.STRING)
    private QuestionLevel questionLevel;

    private  Long sectionId;
    private String status;

    private int outOfMarks;
    private String createdAt;
    private int attemptedQuestions;
    private int notAttemptedQuestions;
    private int correctAnswers;
    private int incorrectAnswers;


    @PrePersist
    public void onCreate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
        this.createdAt = LocalDateTime.now().format(formatter);
    }




}
