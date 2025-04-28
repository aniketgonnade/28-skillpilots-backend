package com.skilladmin.model;


import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class PackagesData {
	
	@Id
	@Column(name = "package_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int package_id;
	private String package_name;
	private Date creation_date;
	private Date updation_date;
	private Date effective_date;
	private Date expiration_date;
	private double amount;
	private int validity;
	private int no_of_internships;
	private String package_type;
	private String package_for;
	private int no_of_dept;
	private String package_desc;
	
	
	

}
