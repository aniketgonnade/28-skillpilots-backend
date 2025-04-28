package com.skilladmin.dto;

import lombok.Data;

@Data
public class StudentListDto {
    private Long studentId;
    private String studentName;
    private String emailId;
    private Long contact;
    private Long collegeId;
    private String collegeName; // Add this field for college name
    private String dept;
    private String departmentName;
    private String year;
    private Long deptId;
}
