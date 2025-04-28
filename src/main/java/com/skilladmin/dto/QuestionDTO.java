package com.skilladmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {

    private Long id;
    private String questionText;
    private Map<String, String> options;
    private String questionType;
    private int marks;
    private int markPerQues;
   // private int passingMarks;
}
