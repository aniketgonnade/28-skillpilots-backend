package com.skilladmin.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skilladmin.model.Company;
import com.skilladmin.repository.CompanyRepository;
import com.skilladmin.service.CompanyService;
@Service
public class CompanyServImpl implements CompanyService{

	@Autowired
	private CompanyRepository companyRepository;
	
	
	@Override
	public List<Company> getIndustries() {		
				return companyRepository.findAll();
	}

	@Override
	public Company companyInfo(String email) {
		return companyRepository.findByEmail(email);
	}

}
