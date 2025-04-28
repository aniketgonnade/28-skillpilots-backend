package com.skilladmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.skilladmin.model.PackagesData;

public interface PackageRepository extends JpaRepository<PackagesData, Long> {
	
	@Query("Select p from PackagesData p where p.package_id=:package_id")
	public PackagesData assignPackage(Long package_id);

}
