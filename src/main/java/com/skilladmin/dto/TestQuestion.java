package com.skilladmin.dto;

import com.skilladmin.enumclass.QuestionLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestQuestion {
    private String questionText;
    private QuestionLevel questionLevel;
    private int marks;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
}
