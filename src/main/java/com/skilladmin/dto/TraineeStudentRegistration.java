package com.skilladmin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.Data;

import java.util.Date;

@Data
public class TraineeStudentRegistration {

    private String name;
    private String email;
    private  Long mobNo;

    private  String gender;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dob;
    @Column(length = 1500)
    private  String address;
    private String designation;

}
