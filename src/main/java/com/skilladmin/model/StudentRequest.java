package com.skilladmin.model;

import java.util.Date;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
@Data
@Entity
@Table(name = "student_req")
public class StudentRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "request_id")
	private Long request_id;
	private Long student_id;
	private Long company_id;
	private Long commonId;
	private Long internship_lead_id;
	private Date creation_date;
	private Date updation_date;
	private Date expiration_date;
	private String request_msg;
	private String rejection_msg;
	private String technology_name;
	private int duration;
	private Long advertisement_id;
	private String approval_status;
	private String college_req_msg;
	private String last_status;
	private String studentRole;

}
