package com.skilladmin.dto;

import java.util.List;

import lombok.Data;

@Data
public class AdvertisementFilter {

	
	private List<String> location;
    private List<String> technology;
    private String stipend; 
    private List<Integer> duration;
}
