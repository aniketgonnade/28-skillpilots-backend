package com.skilladmin.repository;

import java.util.List;

import com.skilladmin.model.CompanyDrive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CompanyDriveRepository extends JpaRepository<CompanyDrive, Long> {

    List<CompanyDrive> findByStatusAndCompanyId(String status, Long companyId);

    CompanyDrive findByCompanyId(Long companyId);

    @Query("SELECT d FROM CompanyDrive d WHERE " +
            "(LOWER(d.skill) LIKE %:search%) OR " +
            "(LOWER(d.location) LIKE %:search%) OR " +
            "(LOWER(d.companyName) LIKE %:search%) OR " +
            "(LOWER(d.jobRole) LIKE %:search%)")
    List<CompanyDrive> findByFilters(@Param("search") String search);
    
    List<CompanyDrive> findByStatus(String Status);







}
