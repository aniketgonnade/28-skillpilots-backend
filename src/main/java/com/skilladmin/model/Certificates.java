package com.skilladmin.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Certificates {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long studentId;
	private String url;
	public Certificates(Long id, Long studentId, String url) {
		super();
		this.id = id;
		this.studentId = studentId;
		this.url = url;
	}
	public Certificates() {
		super();
	}
	
	
	
}
