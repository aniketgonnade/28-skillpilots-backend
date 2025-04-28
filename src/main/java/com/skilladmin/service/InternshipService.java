package com.skilladmin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.skilladmin.model.Internships;


public interface InternshipService {

	 public ArrayList<Internships> getPastOrPendingOrCancelledInternshipsOfStudent(Long student_id,String status);
	    boolean countByStudentIdAndStatusInNative(Long studentId);
	    
	    


	    public List<Map<String, Object>> findByOrderInternshipCompletedList();

}
