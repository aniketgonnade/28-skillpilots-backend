package com.skilladmin.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skilladmin.model.NotificationRequest;
import com.skilladmin.repository.NotificationRepository;
import com.skilladmin.service.NotificationService;

@Service
public class NotificationServiceImpl  implements NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;


	@Override
	public List<NotificationRequest> getNotification(Long receiverId) {
		return notificationRepository.findByReceiverId(receiverId);
	}

	@Override
	public List<NotificationRequest> readAll(List<Long> id) {
		List<NotificationRequest> notifications = notificationRepository.findAllById(id);

		for (NotificationRequest n : notifications) {
			n.setStatus(true);
		}

		return notificationRepository.saveAll(notifications);

	}
}