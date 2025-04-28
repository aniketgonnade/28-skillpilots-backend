package com.skilladmin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skilladmin.model.Certificates;

public interface CertificateRepository extends JpaRepository<Certificates, Long> {

	

	public List<Certificates>  findByStudentId(Long studentid);
	
}
