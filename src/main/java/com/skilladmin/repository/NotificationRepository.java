package com.skilladmin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skilladmin.model.NotificationRequest;

public interface NotificationRepository extends JpaRepository<NotificationRequest, Long> {

	List<NotificationRequest> findByReceiverId(Long receiverId);
	
}
