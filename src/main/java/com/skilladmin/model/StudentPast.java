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
@Table(name="students_past")
public class StudentPast {
	@Id
	@Column(name="stud_past_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long stud_past_id;
	Long student_id;
	String category;
	String profile;
	String organization;
	String board;
	Long department;
	String location;
	String start_date;
	String end_date;
	String description;
	String link;
	Date creation_date;
	Date updation_date;
	String stream;

	
}