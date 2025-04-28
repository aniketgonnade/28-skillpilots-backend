package com.skilladmin.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.skilladmin.model.CompAdvertisement;
import com.skilladmin.repository.CompAdvertisementRepo;
import com.skilladmin.service.CompAdvertismentService;


@Service
public class CompAdvertismentImpl implements CompAdvertismentService {
   
	private final CompAdvertisementRepo advertisementRepo;

	public CompAdvertismentImpl(CompAdvertisementRepo advertisementRepo) {
		this.advertisementRepo=advertisementRepo;
	}
	
	@Override
	public List<CompAdvertisement> getAllAdvertisemnts() {
		return advertisementRepo.findAll();
	}

	@Override
	public Optional<CompAdvertisement> getAdvertisemnet(Long id) {
		return advertisementRepo.findById(id);
	}

	@Override
	public List<CompAdvertisement> getFilter(List<String> location, List<String> technology, Boolean isPaid, List<Integer> duration) {
		return advertisementRepo.findByFilters(location, technology, isPaid, duration);
	}

//	@Override
//	public List<CompAdvertisement> getAllTech() {
//		// TODO Auto-generated method stub
//		return advertisementRepo.findByTechnology();
//	}

}
