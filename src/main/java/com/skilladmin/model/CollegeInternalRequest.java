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
@Table(name = "coll_internal_req")
public class CollegeInternalRequest {
	@Id
	@Column(name = "request_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long request_id;
	private Long user_id;
	private Date creation_date;
	private Date updation_date;
	private String approval_status;
	private String request_msg;
	private String rejection_msg;
	private String for_users;
	private Date expiration_date;
	private String technology;
	private int duration;
	private String for_company;
	private Long advertisement_id;
    private Long against_ext_req;
    private Long for_dept;
    private String last_status;
    

}