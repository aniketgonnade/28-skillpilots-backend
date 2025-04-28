package com.skilladmin.serviceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skilladmin.model.Internships;
import com.skilladmin.repository.InternshipRepository;
import com.skilladmin.service.InternshipService;
@Service
public class InternshipServImpl implements InternshipService {

	@Autowired 
	private InternshipRepository internshipRepository;
	@Override
	public ArrayList<Internships> getPastOrPendingOrCancelledInternshipsOfStudent(Long student_id, String status) {
		return internshipRepository.getPastOrPendingOrCancelledInternshipsOfStudent(student_id, status);
	}
	@Override
	public boolean countByStudentIdAndStatusInNative(Long studentId) {
		 List<String> validStatuses = Arrays.asList("ongoing", "P");
		 Long count = internshipRepository.countByStudentIdAndStatusInNative(studentId, validStatuses);
	        return count != null && count > 0;

}    
	
	@Override
	 public List<Map<String, Object>> findByOrderInternshipCompletedList()
	{
	        List<Map<String, Object>> internships = internshipRepository.getInternshipCompletedList();
	        System.out.println("Internships: " + internships);
	        return internships;
	       
	       
	       
	}
}