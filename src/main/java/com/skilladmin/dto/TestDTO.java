package com.skilladmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class TestDTO {

    private Long testId;
    private String testName;
    private int marksPerQuestion;

    private int timeLimit;
    private List<SectionResponse> sections;
    private int noOfQuestion;
    private int passingMarks;

}
