package com.skilladmin.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@Entity
public class RecruitmentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailId;
    private String companyName;
    private String contactName;
    private String website;
    private String jobTitle;
    @Column(length = 50000)
    private String jobDescription;
    private String isOpen="true";
    private LocalDate interviewStartDate;
    private LocalDate interviewEndDate;
    private String ssc;
    private String hsc;
    private String ug;
    private String city;
    private String skills;

    private String cljEmail;
    private String creationDate;

    @PrePersist
    public void  crateDate(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.creationDate=LocalDate.now().format(dateTimeFormatter);
    }
}
