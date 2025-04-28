package com.skilladmin.model;

import java.time.LocalDate;
import java.util.Date;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "student_data")
@Transactional
public class Student {
	@Id
	@Column(name = "student_id")
	private Long student_id;
	private String student_name;
	private Date creation_date;
	private Date updation_date;

	private LocalDate d_o_b;
	private String email_id;
	private Long contact;
	//private String photo;
	  @Lob
	    @Column(length = 1048576) // Adjust the length as needed
	private byte[] photo;
	private Date effective_date;
	private Date expiration_date;
	private String curr_city;
	private String home_city;
	private String curr_year;
	private String curr_enroll_no;
	private Date valid_till;
	private String skills;
	private String interest;
	private String hobbies;
	private String achievements;
	private String request_status;
	private String device_id;
	private Long college_id;
	private String dept;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="curr_education")
	private StudentPast studentPast;
	
}