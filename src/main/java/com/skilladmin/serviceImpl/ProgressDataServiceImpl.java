package com.skilladmin.serviceImpl;

import org.springframework.stereotype.Service;

import com.skilladmin.model.ProgressData;
import com.skilladmin.repository.ProgressDataRepository;
import com.skilladmin.service.ProgresDataService;

@Service
public class ProgressDataServiceImpl  implements ProgresDataService{

	private final ProgressDataRepository dataRepository;
	
	public ProgressDataServiceImpl( ProgressDataRepository dataRepository2) {
		this.dataRepository = dataRepository2;
	}
	
	
	@Override
	public ProgressData getProgressData(Long internshipId) {
		return dataRepository.getProgressData(internshipId);
	}

}
