package com.skilladmin.model;



import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "notifications")
public class NotificationRequest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long notifId;
	private String title;
	private String msgBody;
	private Long senderId;
	private Long receiverId;
	private Long deptId;
	private LocalDateTime dateTime;
	 private String token;
	  private String topic;
	private  boolean status;



	
	
}
