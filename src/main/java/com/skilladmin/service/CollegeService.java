package com.skilladmin.service;

import java.util.List;

import com.skilladmin.model.College;
import com.skilladmin.model.Company;
import com.skilladmin.model.User;

public interface CollegeService {

	List<Object[]> getAllColleges();
	
	List<Object[]> getAllCompanies();
	
	User updateCollege(Long college_id);
	
	User updateCompany(Long company_id);
	
	List<Object[]> getStudentsWithCollege(String role,Long college_id);
	
	College getCollegeInfo(String email);

	List<College> getColleges();

	
	
}
