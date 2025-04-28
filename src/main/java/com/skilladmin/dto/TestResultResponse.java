package com.skilladmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestResultResponse {


    private Long userId;
    private Long testId;
  //  private Long sectionId;
    private int totalScore;
    private int totalQuestions;
    private int correctAnswers;
    private int cutoffMarks;
    private String status;
    private String createdAt;
    private int attemptedQuestions;
    private int notAttemptedQuestions;
    private int incorrectAnswers;

}
