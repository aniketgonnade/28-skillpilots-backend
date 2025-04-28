package com.skilladmin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.skilladmin.model.CompAdvertisement;


public interface CompAdvertisementRepo extends JpaRepository<CompAdvertisement, Long>{

	
//	@Query("SELECT c FROM CompAdvertisement c WHERE "
//		     + "(:location IS NULL OR :location = '0' OR c.location IN :location) AND "
//		     + "(:technology IS NULL OR :technology = '0' OR c.technology IN :technology) AND "
//		     + "(:isPaid IS NULL OR (c.stipend > 0 AND :isPaid = true) OR (c.stipend = 0 AND :isPaid = false)) AND "
//		     + "(:duration IS NULL OR :duration = 0 OR c.duration IN :duration)")
//		List<CompAdvertisement> findByFilters(
//		    @Param("location") List<String> location,
//		    @Param("technology") List<String> technology,
//		    @Param("isPaid") Boolean isPaid,
//		    @Param("duration") List<Integer> duration
//		);
	@Query("SELECT c FROM CompAdvertisement c WHERE "
		       + "(:location IS NULL OR :location = '' OR c.location IN :location) AND "
		       + "(:technology IS NULL OR :technology = '' OR c.technology IN :technology) AND "
		       + "(:isPaid IS NULL OR (c.stipend > 0 AND :isPaid = true) OR (c.stipend = 0 AND :isPaid = false)) AND "
		       + "(:duration IS NULL OR :duration = '' OR c.duration IN :duration)")
		List<CompAdvertisement> findByFilters(
		    @Param("location") List<String> location,
		    @Param("technology") List<String> technology,
		    @Param("isPaid") Boolean isPaid,
		    @Param("duration") List<Integer> duration
		);

	
	
	@Query("Select Distinct c.technology From CompAdvertisement c ")
	List<String> getTechnology();


	@Query("select Distinct c.location From CompAdvertisement c ")
	List<String> getLocation();
}
