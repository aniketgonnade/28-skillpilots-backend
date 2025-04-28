package com.skilladmin.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDrive {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long driveId;
	private  String companyName;
	private String skill;
	private String jobRole;
	private String  ctc;
	private String interviewDate;
	@Column(length = 50000)
	private String jobDescription;
	private String location;
	
	private String creationDate;
	private String status= "A";
	private String ssc;
	private String hsc;
	private String ug;
	private String experience;


	private Long companyId;
	
	@PrePersist
	public void date() {
		
		DateTimeFormatter dateTimeFormatter= DateTimeFormatter.ofPattern("yyyy-MM-dd");
		this.creationDate=LocalDate.now().format(dateTimeFormatter);
	}
	
	
	

}
