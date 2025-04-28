package com.skilladmin.model;

import java.time.LocalDate;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "college_data4")
public class College {
    @Id
    @Column(name = "college_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long college_id;
    private String college_name;
    private String registration_no;
    private LocalDate creation_date; // Updated to LocalDate
    private LocalDate updation_date; // Updated to LocalDate
    private LocalDate estd_date;      // Updated to LocalDate
    private LocalDate expiration_date; // Updated to LocalDate
    private LocalDate effective_date;  // Updated to LocalDate
    private String line_1;
	private String line_2;
	private String city;
	private String state;
	private String country;
	private Integer pincode;
    private Long contact_1;
    private Long contact_2;
    private String web_url;
    private String email_id;
    private short type;
    @Lob
    @Column(length = 1048576)
    private byte[] logo;
    @Column(columnDefinition = "VARCHAR(255) DEFAULT 'pending'")
    private String status;

    
    
    
}