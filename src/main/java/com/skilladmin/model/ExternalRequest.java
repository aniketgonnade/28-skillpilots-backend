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
@Table(name = "external_req")
public class ExternalRequest {
	@Id
	@Column(name = "request_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long request_id;
	private Long from_user_id;
	private String for_user_id;
	private Date creation_date;
	private Date updation_date;
	private String approval_status;
	private String request_msg;
	private String rejection_msg;
    private Date expiration_date;
    private String technology;
	private int duration;
	private String student_ids;
	private Long advertisement_id;
	private String last_status;
	private String type;
	private Long company_id;


}