package com.skilladmin.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skilladmin.model.Rating;
import com.skilladmin.service.RatingService;

@RestController
@RequestMapping("/rating")
@CrossOrigin(origins = {"*"})
public class RatingController {

	@Autowired
	private RatingService ratingService;

	// RATING FOR MOBILE APP
	@PostMapping
	public ResponseEntity<Rating> submitFeedback(@RequestBody Rating rating) {
		Rating savedFeedback = ratingService.addRating(rating);
		return ResponseEntity.ok(savedFeedback);
	}

	// GET RATINGS
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>>getAllRating() {
		return ResponseEntity.ok(ratingService.getAllRating());
	}

	
	
}
