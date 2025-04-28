package com.skilladmin.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;

import com.skilladmin.model.CompAdvertisement;


public interface CompAdvertismentService {

	public List<CompAdvertisement> getAllAdvertisemnts();
	
	public Optional<CompAdvertisement> getAdvertisemnet(Long id);
	
	public List<CompAdvertisement> 
	getFilter(List<String> location, List<String> technology, Boolean isPaid, List<Integer> duration) ;

	
//	public List<CompAdvertisement> getAllTech();
	
}
