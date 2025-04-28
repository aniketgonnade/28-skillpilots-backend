package com.skilladmin.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CampusResultRepository extends JpaRepository<CampusResult, Long>{
	
	@Query("select cd.name,cr.resultStatus,cr.totalMarks,cr.cuttoff from CampusResult cr left join CampusDrive cd on cd.id=cr.studentId ")
	public List<Object> getCampusResult();

}
