package com.skilladmin.service;

import java.util.List;

import com.skilladmin.model.NotificationRequest;

public interface NotificationService {

	
	List<NotificationRequest> getNotification(Long receiverId);
	List<NotificationRequest> readAll(List<Long> id);
}
