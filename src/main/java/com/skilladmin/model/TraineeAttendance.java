package com.skilladmin.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class TraineeAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    private LocalDate localDate;

    private boolean status;

    private String localDateTime;

    @ManyToOne()
    @JoinColumn(name = "studentId")
    private  TrainingUsers  trainingUsers;

    private  Long batchId;


}
