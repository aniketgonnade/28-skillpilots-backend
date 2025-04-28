package com.skilladmin.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Data
public class TraineeStudentMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long studyMaterialId;
    private  String topic;
    private String description;
    private  String uploadUrl;

    private  Long batchId;

    private Long collegeId;
    private Long deptId;
    private String createdDate;

    @PrePersist
    public  void  onCreate(){
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.createdDate= LocalDate.now().format(formatter);
    }

}
