package com.skilladmin.model;

import java.time.LocalDate;
import java.util.Date;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "internships2")
public class Internships {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "internship_id")
	private Long internship_id;
	private Long commonId;
	private Long coll_dept;
	private String comp_dept_id;
	private String group_id;
	private String reason;
	private Long student_id;
	private String technology;
	private Long college_id;

	private Long company_id;
	private String companyName;
	private int duration;
	private LocalDate creation_date = LocalDate.now();
	private LocalDate updation_date;
	private Date date_started;
	private Date date_completed;
	private Long advertisement_id;
	private String status  = "pending";
	private Long against_int_req_id;
	private short type;
	@Column(length = 500)
	private String downloadUrl;
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	
	

}