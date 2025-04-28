package com.skilladmin.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
@Entity
@Data
public class Address {

	
	@Id
	@Column(name = "address_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long address_id;
	private String line_1;
	private String line_2;
	private String city;
	private String state;
	private String country;
	private Integer pincode;
	private LocalDate creation_date;
	private LocalDate updation_date;
	@Override
	public String toString() {
		return "Address [address_id=" + address_id + ", line_1=" + line_1 + ", line_2=" + line_2 + ", city=" + city
				+ ", state=" + state + ", country=" + country + ", pincode=" + pincode + ", creation_date="
				+ creation_date + ", updation_date=" + updation_date + "]";
	}

}
