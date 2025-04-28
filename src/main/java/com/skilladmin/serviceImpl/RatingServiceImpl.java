package com.skilladmin.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skilladmin.model.Rating;
import com.skilladmin.repository.RatingRepository;
import com.skilladmin.service.RatingService;

@Service
public class RatingServiceImpl implements RatingService {

	@Autowired
	private RatingRepository ratingRepository;


	@Override
	public Rating addRating(Rating rating) {
		return ratingRepository.save(rating);
	}


	// get all rating 27/03/25
	@Override
	public List<Map<String, Object>> getAllRating() {
		List<Object[]> results = ratingRepository.findTop5ByOrderByIdDescNative();
		List<Map<String, Object>> formattedResults = new ArrayList<>();

		for (Object[] row : results) {
			Map<String, Object> map = new HashMap<>();
			map.put("username", row[0]);
			map.put("ratingId", row[1]);
			map.put("param1", row[2]);
			map.put("param2", row[3]);
			map.put("param3", row[4]);
			map.put("param4", row[5]);
			map.put("param5", row[6]);
			formattedResults.add(map);

			System.out.println("Username: " + row[0] +
					", Rating ID: " + row[1] +
					", Param1: " + row[2] +
					", Param2: " + row[3] +
					", Param3: " + row[4] +
					", Param4: " + row[5] +
					", Param5: " + row[6]);
		}

		return formattedResults;
	}


}