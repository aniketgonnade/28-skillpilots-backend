package com.skilladmin.model;

import java.time.LocalDate;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
@Entity
@Data
@Table(name = "company_data2")

public class Company {
	
	@Id
	@Column(name = "company_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long company_id;
	private String company_name;
	private LocalDate creation_date;
	private LocalDate updation_date;
	private LocalDate estd_date;
	  @Lob
	    @Column(length = 1048576)
	private byte[] logo;
	private Long contact_1;
	private Long contact_2;
	private String web_url;
	private String email_id;
	private String registration_no;
	@Column(columnDefinition = "LONGTEXT")
	private String about;
	private String line_1;
	private String line_2;
	private String city;
	private String state;
	private String country;
	private Integer pincode;
	public LocalDate expiration_date;
	private short type;

	
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="address_id")
	private Address address;
	private String photo;
	private String sign;

}
