package com.skilladmin.dto;

import com.skilladmin.enumclass.QuestionLevel;
import com.skilladmin.enumclass.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionResponse {

    private Long id;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private QuestionType questionType;
    private int marks;
    private QuestionLevel questionLevel;
}
