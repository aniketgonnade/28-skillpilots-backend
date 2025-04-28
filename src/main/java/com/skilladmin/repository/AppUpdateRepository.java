package com.skilladmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skilladmin.model.AppUpdate;

public interface AppUpdateRepository extends JpaRepository<AppUpdate, Long>{
	
	public AppUpdate findByVersionCode(String versionCode);

}
