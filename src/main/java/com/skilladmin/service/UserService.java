package com.skilladmin.service;

import com.skilladmin.dto.AdminDto;
import com.skilladmin.model.Tutors;
import com.skilladmin.model.User;
import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {

	
	public void sendVerificationEmail(String toEmail, String subject, String content) ;
	
	public User saveUser(User user);
	
	public User getHodByInstituteId(Long instituteId);
	public User getHrByInstituteId(Long instituteId);
	
	public Long getStatus(Long id);

	boolean emailExists(String email);
	int findByHodVerified(Long studentId);

	List<Map<String, Object>> findManagerAndLead(@Param("company_id") Long companyId);


	User editAdmin(Long adminId, AdminDto adminDto);

	public List<Map<String, Object>> getUsersWithRole20();


}
