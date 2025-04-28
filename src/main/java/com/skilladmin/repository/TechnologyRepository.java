package com.skilladmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skilladmin.model.Technologies;

public interface TechnologyRepository extends JpaRepository<Technologies, Long> {

}
