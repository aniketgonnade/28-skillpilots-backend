package com.skilladmin.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.skilladmin.dto.*;
import com.skilladmin.model.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.skilladmin.config.StudentDto;


public interface StudentService {

	public Student saveStudent(Student student);
	
	public StudentDto login(StudentDto studentDto);
	public StudentDto register(Student student, String role,int college_id,User user,StudentPast studentPast,Long id,Long department) ;

	public StudentRequest applyRequest(StudentRequest studentRequest);
	
	public List<StudentRequest> findRequest(Long request_id);

	public Student editStudent(Student student,Long student_id);
	
	public Student addPhoto(Long student_id,MultipartFile file);
	
	public User changePassword(Long id,String password,String newPass);
	
	public byte[] getPhoto(Long studentId);
	 
	public Map<String, Object> getStudent(Long studentId);
	
	public User forgotPassword(String email);
	
	public User resetPass(Long studentId,String otp,String newPass) throws Exception;
	public List<CollegeInternalReqDto> getClgInternalReqOfStudentWithDept(Long student, Long student_id,Long college_id,Date expiration_date,String status);
	public ExternalRequest getExternalRequestByERid(Long request_id) ;

	public StudentPast editPast(Long studentId,StudentPast studentPast);
	public StudentRequest getStudentReqAgainstAdvNotPresent(Long id);
	
    public void updateStudentAndPast(Long studentId, StudentUpdateDto dto,Long studpastId) ;
    
    public StudentPast addStudentPast(Long studentId, StudentUpdateDto dto);
    
	List<InternshipDto> getCertificate(Long id);

	public Page<StudentListDto> getAllStudentsList(int page, int size);


	public RecruitmentStatus applyDrive(Long studentId, Long recruitId, Long collegeId);

	public List<RecruitmentStatus> findAppicanCount();

	public RecruitmentStatus changeStatus(Long statusId,String status);

	public Object sendRecruitmentToStudent(List<Long> studentIds,Long recruitmentId,Long collegeId);

	public List<RecruitmentStatus> updateRound(List<Long> studentIds,Long recruitmentId,String round,String roundTime,String roundDate);

	public List<RecruitmentWithStatusDTO> getRecruitmentsWithStatus(Long collegeId, Long studentId);

	public List<RecruitmentStatus> updateCompanyRound(List<Long> studentIds,Long driveId,String round,String roundTime,String roundDate,List<Long> id,String meetLink);
	public Page<StudentListDto> searchStudentsByName(String name,int page,int size);

	public List<StudentListDto> getStudentList();
	public List<StudentListDto> getStudentsWithoutFilter(Long collegeId);

	public List<StudentListDto> getDeptAndCollegeWithoutFilter(Long collegeId,Long deptId,String year);

	public Page<StudentListDto> getByDeptAndColege(int page,int size,Long collegeId,Long deptId);
	public Page<StudentListDto>getInternalStudent(int page,int size,Long collegeId);
	//Page<StudentListDto> getCollegeAndDeptDetails(int page, int size, Long collegeId, Long deptId, String role);
}


