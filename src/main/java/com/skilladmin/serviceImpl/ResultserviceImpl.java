package com.skilladmin.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skilladmin.dto.ResultDto;
import com.skilladmin.model.Results;
import com.skilladmin.repository.ResultRepository;
import com.skilladmin.service.ResultService;

@Service
public class ResultserviceImpl implements ResultService {

	@Autowired
	private ResultRepository resultRepository;
	
	@Override
	public Results saveResult(Results results) {
		return resultRepository.save(results);
	}

	@Override
	public List<ResultDto> getTestHistory(Long studentId) {
	    List<Object[]> testHistory = resultRepository.getTestHistory(studentId);
	    
	    ArrayList<ResultDto> arrayList = new ArrayList<>();
	    for (Object[] obj : testHistory) {
	        ResultDto resultDto = new ResultDto();
	        resultDto.setMarks((int) obj[0]);
	        resultDto.setResultStatus((String) obj[1]);  // Fixed mapping here
	        resultDto.setTestName((String) obj[2]);
	        resultDto.setCorrectAns((int) obj[3]);
	        resultDto.setOutof((int) obj[4]);
	        arrayList.add(resultDto);
	    }
	    return arrayList;
	}

}
