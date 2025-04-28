package com.skilladmin.service;

import java.util.List;

import com.skilladmin.dto.ResultDto;
import com.skilladmin.model.Results;

public interface ResultService {
	 public Results saveResult(Results results);
	 
	 public List<ResultDto> getTestHistory(Long studentId);
}
