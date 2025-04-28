package com.skilladmin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudentPerformanceDTO {

    private Long studentId;
    private String studentName;
    private String sscDescription;
    private String hscDescription;
    private String ugDescription;
    private String status;
    private String passingYear;

    public StudentPerformanceDTO(Long studentId, String studentName, String sscDescription, String hscDescription, String ugDescription) {
    }
}
