package com.skilladmin.model;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "progress_data")
public class ProgressData {
	@Id
	@Column(name = "progress_data_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long progress_data_id;
	private String parameter_1;
	private String parameter_2;
	private String parameter_3;
	private String parameter_4;
	private String parameter_5;
	private String parameter_6;
	private String parameter_7;
	private String parameter_8;
	private String overall;
	private String status;
	private String comments;
	private Long internship_id;
	private Date creation_date;
	private Date updation_date;
	private Date effective_date;
	private Date expiration_date;
	
}
