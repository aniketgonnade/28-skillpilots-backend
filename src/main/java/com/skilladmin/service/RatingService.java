package com.skilladmin.service;

import java.util.List;
import java.util.Map;

import com.skilladmin.model.Rating;

public interface RatingService {
	
	
	public Rating addRating(Rating rating);

	List<Map<String, Object>> getAllRating();

}
