package com.skilladmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttemptDTO {

    private String level;
    private int obtainedMarks;
    private int totalMarks;
    private String date;
    private String status;

}
