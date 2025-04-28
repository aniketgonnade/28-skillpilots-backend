package com.skilladmin.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
@Data
@Entity
@Table(name = "internship_schedule")
public class InternshipSchedule {
	@Id
	@Column(name="schedule_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int schedule_id;
	private String internship_group_id;
	public String getGroup_id() {
		return internship_group_id;
	}
	public void setGroup_id(String group_id) {
		this.internship_group_id = group_id;
	}

	private String schedule_type;
	private String schedule_1;
	private String schedule_2;
	private String schedule_3;
	private String schedule_4;
	@ManyToOne
	@JoinColumn(name="advertisement_id")
	private CompAdvertisement compAdvertisement;
}
