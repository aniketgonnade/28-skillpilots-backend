package com.skilladmin.dto;

import lombok.Data;

import java.util.List;
@Data
public class AnswerDTO {

    private Long questionId;
    private List<String> answer;
}
