package com.skilladmin.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class TraineeUpdateDto {

    private  String name;
    private Long mobNo;
    private  String gender;
    private Date dob;
    @Column(length = 1000)
    private String address;
private String designation;
    private String skills;
    private String interest;


}
