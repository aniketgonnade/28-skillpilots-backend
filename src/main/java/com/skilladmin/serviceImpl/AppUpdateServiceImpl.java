package com.skilladmin.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skilladmin.model.AppUpdate;
import com.skilladmin.repository.AppUpdateRepository;
import com.skilladmin.service.AppUpdateService;

@Service
public class AppUpdateServiceImpl implements AppUpdateService{

	@Autowired
	public AppUpdateRepository appUpdateRepository;
	
	
	@Override
	public List<AppUpdate> findByVersionId() {
		return appUpdateRepository.findAll();
	}

}
