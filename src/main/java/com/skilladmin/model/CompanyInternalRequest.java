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
@Table(name = "comp_internal_req")
public class CompanyInternalRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="request_id")
	private Long request_id;
	private Long user_id;
	private Date creation_date;
	private Date updation_date;
	private Date expiration_date;
	private Long company_id;
	
	public Date getExpiration_date() {
		return expiration_date;
	}
	public void setExpiration_date(Date expiration_date) {
		this.expiration_date = expiration_date;
	}
	private String request_msg;
	private String rejection_msg;
	private Long for_college;
	private String technology;
	private int duration;
	private Long advertisement_id;
	private String for_team;
	private Long against_ext_req;
	private String for_users;
	private String approval_status;
	private Long comp_balance_id;
	private String coll_balance_id;
	
	private  String userType;


	
	
}

