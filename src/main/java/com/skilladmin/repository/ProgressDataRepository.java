package com.skilladmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.skilladmin.model.ProgressData;

public interface ProgressDataRepository extends JpaRepository<ProgressData, Long> {

	
	
	@Query(value="select * from progress_data where internship_id=:internship_id", nativeQuery = true)
	public ProgressData getProgressData(Long internship_id);
}
