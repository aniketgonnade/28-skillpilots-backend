package com.skilladmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.skilladmin.model.College;

public interface CollegeRepository extends JpaRepository<College, Long> {
	
	@Query("Select c from College c where c.email_id=:email_id")
	public College findByEmail(String email_id);

	
	
}
