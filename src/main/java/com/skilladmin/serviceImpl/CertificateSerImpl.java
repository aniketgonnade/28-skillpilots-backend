package com.skilladmin.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skilladmin.model.Certificates;
import com.skilladmin.repository.CertificateRepository;
import com.skilladmin.service.CertificateService;

@Service
public class CertificateSerImpl implements CertificateService {

	@Autowired
	private CertificateRepository certificateRepository;
	
	
	
	
	
	@Override
	public List<Certificates> getUrl(Long studentId) {
		
		return certificateRepository.findByStudentId(studentId);
	}

}
