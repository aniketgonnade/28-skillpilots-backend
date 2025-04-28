package com.skilladmin.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "advertisements_comp")
public class CompAdvertisement {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "company_id")
	private Long companyId;
	private Long commonId;
	private String companyName;
	private String technology;
	private LocalDate creation_date;
	private LocalDate updation_date;
//	@JsonFormat(pattern=ProjectConstants.date_format)
	private LocalDate effective_date;
//	@JsonFormat(pattern=ProjectConstants.date_format)
	private LocalDate expiration_date;
	@Lob
	@Column(columnDefinition = "TEXT")
	private String description;
	private Integer capacity;
	private Integer duration;
	private String status;
	private double stipend;
	
	private LocalDate start_date;
	private String adv_title;
	private String requirement;
	private String location;
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL,mappedBy="compAdvertisement")
	private List<InternshipSchedule> internshipSchedule;
	
}
