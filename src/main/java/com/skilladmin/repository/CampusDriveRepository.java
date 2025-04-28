package com.skilladmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skilladmin.model.CampusDrive;

public interface CampusDriveRepository extends JpaRepository<CampusDrive, Long>{

	CampusDrive findByEmailId(String email);

}
