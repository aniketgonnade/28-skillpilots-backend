package com.skilladmin.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Data
public class Recruitment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long placementId;

    private String companyName;

    private Long contactNo;
    private String email;
    @Column(length = 1000)
    private String address;
    @Column(length = 1000)
    private String profile;

    private String ctc;
    private String departmentIds;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "details_id", referencedColumnName = "detailId")
    private RecruitmentDetails placementDetails;

    private String creationDate;
    private String status="A";

    private Long collegeId;

    @PrePersist
    public void  crateDate(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.creationDate= LocalDate.now().format(dateTimeFormatter);
    }


}
