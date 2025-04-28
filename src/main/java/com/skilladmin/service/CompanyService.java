package com.skilladmin.service;

import java.util.List;

import com.skilladmin.model.Company;

public interface CompanyService {
	
	public List<Company> getIndustries();

	public Company companyInfo(String email);

}
