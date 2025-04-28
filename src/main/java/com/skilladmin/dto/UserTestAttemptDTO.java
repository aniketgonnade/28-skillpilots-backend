package com.skilladmin.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.skilladmin.enumclass.QuestionLevel;
import com.skilladmin.model.UserTestPreference;
import com.skilladmin.model.UserTestResult;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
public class UserTestAttemptDTO {

    private Long id;
    private Long studentId;
    private String testName;  // New field for test name
    private int score;
    private boolean passed;
    private QuestionLevel questionLevel;
    private String status;
    private int outOfMarks;
    private String createdAt;




}
