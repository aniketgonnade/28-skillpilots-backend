package com.skilladmin.service;

import java.util.List;

import com.skilladmin.model.Certificates;

public interface CertificateService {

	
	public List<Certificates> getUrl(Long studentId);
}
