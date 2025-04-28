package com.skilladmin.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Data
public class RecruitmentStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long statusId;
    private String status;
    private String applyDate;
    private Long studentId;
    private String round;
    private String roundTime;
    private String roundDate;
    private Long collegeId;
    private Long companyId;
    private Long driveId;
    private Long reqruitmentId;
    @Column(name = "round_date_time")
    private LocalDateTime roundDateTime;

    @PrePersist
    private void creatDate(){
      DateTimeFormatter formatter=  DateTimeFormatter.ofPattern("yyyy-MM-dd");
      this.applyDate= LocalDate.now().format(formatter);
    }
}

