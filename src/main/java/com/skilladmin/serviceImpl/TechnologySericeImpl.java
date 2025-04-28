package com.skilladmin.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skilladmin.model.Technologies;
import com.skilladmin.repository.TechnologyRepository;
import com.skilladmin.service.TechnologyService;
@Service
public class TechnologySericeImpl implements TechnologyService {

	@Autowired
	private TechnologyRepository technologyRepository;
	
	@Override
	public Technologies createTechnologies(Technologies technologies) {
		return technologyRepository.save(technologies);
	}

	@Override
	public List<Technologies> getTechnologies() {
		return technologyRepository.findAll();
	}

	@Override
	public Technologies findTechnologyById(Long tech_id) {
		return technologyRepository.findById(tech_id).orElseThrow(()-> new RuntimeException("Technolgy Not found"+tech_id));
	}

	@Override
	public void deleteById(Long tech_id) {
		 technologyRepository.deleteById(tech_id);;
	}

}
