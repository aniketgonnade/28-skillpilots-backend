package com.skilladmin.model;

import java.util.Date;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
@Entity
@Data
@Table(name = "feedback_data")
public class FeedbackData {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Assuming 'feedback_id' is auto-incrementing
	@Column(name = "feedback_id")
	Long feedback_id;
	
	Long internship_id;
	private Long feedback_for;
	private Long feedback_from;
	private Date creation_date;
	private Date updation_date;
	private Date effective_date;
	private Date expiration_date;
	private String parameter_1;
	private String parameter_2;
	private String parameter_3;
	private String parameter_4;
	private String parameter_5;
	private String parameter_6;
	private String parameter_7;
	private String parameter_8;
	private String comments;
	private String overall;
	

}
