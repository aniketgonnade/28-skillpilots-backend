package com.skilladmin.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TraineeFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackId;
    private String week;
    private  String param1;

    private String param2;
    private  String param3;
    private  String param4;
    private  String param5;
    private  String param6;
    private  String param7;
    private  String param8;
    @Column(length = 1500)
    private String remarks;

    private Long studentId;

}
