package com.skilladmin.service;

import java.util.List;

import com.skilladmin.model.Technologies;

public interface TechnologyService {
	
	
	public Technologies createTechnologies(Technologies technologies);

	public List<Technologies> getTechnologies();

	public Technologies findTechnologyById(Long tech_id);

	public void deleteById(Long tech_id);
	

}
