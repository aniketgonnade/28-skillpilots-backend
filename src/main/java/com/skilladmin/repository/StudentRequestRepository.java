package com.skilladmin.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.skilladmin.model.StudentRequest;


public interface StudentRequestRepository extends JpaRepository<StudentRequest, Long> {

	@Query("select s from StudentRequest s where s.student_id=:student_id")
	public List<StudentRequest> getStudentRequest(@Param("student_id") Long student_id);


	@Query(value = "SELECT std_req.student_id, std_req.company_id, std_req.request_msg, "
	        + "std_req.rejection_msg, std_req.technology_name, std_req.duration, std_req.advertisement_id, "
	        + "std_req.request_id, std_req.creation_date, std_req.approval_status, st.student_name, "
	        + "cp.company_name, stdt.test_data_id, stdt.against_ext_req, stdt.expiration_date, stdt.status "
	        + "FROM student_req std_req "
	        + "INNER JOIN student_data st ON st.student_id = std_req.student_id "
	        + "LEFT JOIN company_data2 cp ON cp.company_id = std_req.company_id "
	        + "LEFT JOIN student_test_data stdt ON stdt.against_ext_req = std_req.request_id "
	        + "WHERE std_req.student_id = :studentId "
	        + "ORDER BY std_req.request_id", nativeQuery = true)
	List<Object[]> getAppliedStudentsOfCompany(@Param("studentId") Long studentId);

	  @Query(value="SELECT * FROM student_req WHERE student_id = :id", nativeQuery = true)
	  public StudentRequest getStudentReqAgainstAdvNotPresent(Long id);

	  @Query("SELECT COALESCE(COUNT(sr), 0) FROM StudentRequest sr " +
	           "WHERE YEAR(sr.creation_date) = YEAR(:date) " +
	           "AND MONTH(sr.creation_date) = MONTH(:date)")
	    Long countRequestsInMonth(@Param("date") Date date);
}
