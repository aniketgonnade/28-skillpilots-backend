package com.skilladmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skilladmin.model.ExternalRequest;

public interface ExternalRepository extends JpaRepository<ExternalRequest, Long> {

}
