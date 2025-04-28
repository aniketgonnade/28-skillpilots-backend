package com.skilladmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.skilladmin.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
	
	@Query("Select c from Company c where c.email_id=:email_id")
	public Company findByEmail(String email_id);

}
