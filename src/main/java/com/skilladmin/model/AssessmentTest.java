package com.skilladmin.model;

import java.util.Date;
import java.util.List;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
@Data
@Entity
public class AssessmentTest {
	
	@Id
	@Column(name="test_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long tesId;
	public String testName;
	public String userId;
	public int total_mark;
	public int total_que;
	public int time;
	public int cut_off;
	public int mark_per_que;
	public boolean status;
	
	public Date creation_date;
	public Date updation_date;
	public Date expiration_date;

	@OneToMany(mappedBy = "assessmentTest", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Questions> questions;

	@Override
	public String toString() {
		return "AssessmentTest [tesId=" + tesId + ", testName=" + testName + ", userId=" + userId + ", total_mark="
				+ total_mark + ", total_que=" + total_que + ", time=" + time + ", cut_off=" + cut_off
				+ ", mark_per_que=" + mark_per_que + ", status=" + status + ", creation_date=" + creation_date
				+ ", updation_date=" + updation_date + ", expiration_date=" + expiration_date + ", questions="
				+ questions + "]";
	}
}
