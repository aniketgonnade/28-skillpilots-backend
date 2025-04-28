package com.skilladmin.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import com.skilladmin.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	
	@Query("SELECT ru.id, ru.username, ru.contact_no, ru.verified, bd.no_of_internships, " +
		       "cd.email_id, bd.expiration_date, pd.package_name, bd.creation_date, " +
		       "COALESCE(ps.status, 'NA') AS payment_status, " +  // Fixed comma issue
		       "cd.college_id " +  
		       "FROM User ru " +
		       "JOIN College cd ON ru.email = cd.email_id " +
		       "JOIN BalanceData bd ON bd.user_id = cd.college_id " +
		       "JOIN PackagesData pd ON pd.package_id = bd.package_id " +
		       "LEFT JOIN PaymentStatus ps ON ps.collegeId = cd.college_id " + 
		       "WHERE ru.role = '5' and bd.balancefor='College'")
		List<Object[]> findAllCollege();

 


     @Query("SELECT ru.id, ru.username, ru.contact_no, ru.verified, bd.no_of_internships, " +
             "cd.email_id, bd.expiration_date, pd.package_name, bd.creation_date " +
             "FROM User ru " +
             "JOIN Company cd ON ru.email = cd.email_id " +
             "JOIN BalanceData bd ON bd.user_id = cd.company_id " +
             "JOIN PackagesData pd ON pd.package_id = bd.package_id " +
             "WHERE ru.role = '1' and balancefor='Compony' ")
	List<Object[]> findAllCompany();
	
	@Query(value = "SELECT * FROM user  WHERE common_id = :instituteId ", nativeQuery = true)
	User findByCommanId(Long instituteId);


	@Query(value = "SELECT * FROM user  WHERE common_id = :instituteId ", nativeQuery = true)
	User findByCommanId2(Long instituteId);
	
	@Query("select u.username, u.email, u.contact_no, u.verified, s.creation_date " +
		       "from User u " +
		       "LEFT JOIN College c on u.college_id = c.college_id " +
		       "LEFT JOIN Student s on u.id = s.student_id " +
		       "where u.role = :role and c.college_id = :college_id")
	List<Object[]> getStudentWithCollege(String role ,Long college_id);

	@Query("select u.username, u.email, u.contact_no, u.verified, s.creation_date " +
		       "from User u " +
		       "LEFT JOIN Student s on u.id = s.student_id " +
		       "where u.role = :role")
		List<Object[]> getExternalStudents(String role);

	@Query("select u.username, u.email, s.creation_date, s.curr_year, u.department " +
			"from User u " +
			"LEFT JOIN Student s on u.id = s.student_id " +
			"where u.role = :role ")
	Page<Object[]> getExteStudents(String role,Pageable pageable);

	@Query("select u.username, u.email, s.creation_date, s.curr_year, u.department " +
			"from User u " +
			"LEFT JOIN Student s on u.id = s.student_id " +
			"where u.role = :role ")
   List<Object[]>	getExteStudentsWithoutFilter (String role);

	@Query("select u.username, u.email, s.creation_date, s.curr_year, c.college_name, u.department, u.college_id " +
			"from User u " +
			"LEFT JOIN College c on u.college_id = c.college_id " +
			"LEFT JOIN Student s on u.id = s.student_id " +
			"where u.role = :role and c.college_id = :collegeId ")
	Page<Object[]> getInternalStudent(@Param("collegeId") Long collegeId,
										 @Param("role") String role,
										 Pageable pageable);

	@Query("select u.username, u.email, s.creation_date, s.curr_year, c.college_name, u.department, u.college_id " +
			"from User u " +
			"LEFT JOIN College c on u.college_id = c.college_id " +
			"LEFT JOIN Student s on u.id = s.student_id " +
			"where u.role = :role and c.college_id = :collegeId ")
	List<Object[]> getInternalStudentNoFilter (@Param("collegeId") Long collegeId,@Param("role") String role);


    @Query("select u.verified from User u where u.id =:id")
     Long findStatus(Long id);
	@Query("select u.username, u.email, s.creation_date, s.curr_year, c.college_name, u.department " +
			"from User u " +
			"LEFT JOIN College c on u.college_id = c.college_id " +
			"LEFT JOIN Student s on u.id = s.student_id " +
			"where u.role = :role and c.college_id = :collegeId and u.department = :deptId")
	Page<Object[]> getCollegeAndDeptData(@Param("collegeId") Long collegeId,
										 @Param("deptId") Long deptId,
										 @Param("role") String role,
										 Pageable pageable);
	@Query("SELECT u.username, u.email, s.creation_date, s.curr_year, c.college_name, u.department, d.dept_name " +
			"FROM User u " +
			"INNER JOIN College c ON u.college_id = c.college_id " +
			"INNER JOIN Department d ON d.deptId = u.department " +
			"LEFT JOIN Student s ON u.id = s.student_id " +
			"WHERE u.role = :role " +  // Mandatory role match
			"AND c.college_id = :collegeId " +  // Mandatory college match
			"AND (:deptId IS NULL OR u.department = :deptId) " +  // Optional department filter
			"AND (:year IS NULL OR s.curr_year = :year)")  // Optional year filter
	List<Object[]> getCollegeAndDeptWithoutFilter(
			@Param("collegeId") Long collegeId,
			@Param("deptId") Long deptId,
			@Param("role") String role,
			@Param("year") String year
	);



	User findByEmail(String username);

	@Query("select u.hodverified from User u where u.id =:studentId")
	 int findByHodVerified(Long studentId);

	@Query(value=" select * from user where role=:role and email_id_common=:emailId ",nativeQuery = true)
    User findByEmailAndRole(String role,@Param("emailId")String email);
	@Query(value = "SELECT * FROM user WHERE (role = 7 OR role = 8) AND department = :deptId AND email_id_common = :email_id ", nativeQuery = true)
	List<User> findByEmailIdCommonForNotification(String email_id, Long deptId);
	@Query(value = "SELECT * FROM user WHERE role = 2 AND email_id_common = :email ", nativeQuery = true)
	Optional<User> findByEmailAndRole2(String email);



	@Query("select u from User u where u.college_id=:collegeId and u.role='0' or u.role='20'")
	List<User> findCollegeStudent(int collegeId);


	@Query(value = "SELECT u.username, u.email, u.id, r.apply_date, r.status, ru.company_name, ru.placement_id " +
			"FROM user u " +
			"JOIN recruitment_status r ON r.student_id = u.id " +
			"LEFT JOIN recruitment ru ON ru.placement_id = r.reqruitment_id  " +
			"WHERE r.college_id = :collegeId and r.reqruitment_id = :reqruitmentId and r.status = :status",
			nativeQuery = true)
	List<Map<String, Object>> findUserRecruitmentDetails(@Param("collegeId") Long collegeId,
			@Param("reqruitmentId") Long reqruitmentId, @Param("status") String status);

	@Query(value = "SELECT u.username, u.email, u.id, r.apply_date, r.status, ru.company_name, ru.drive_id, r.company_id " +
			"FROM user u " +
			"JOIN recruitment_status r ON r.student_id = u.id " +
			"LEFT JOIN company_drive ru ON ru.drive_id = r.drive_id  " +
			"WHERE r.company_id = :company_id and r.drive_id = :drive_id and r.status = :status",
			nativeQuery = true)
	List<Map<String, Object>> findCompanyrecruitment(@Param("company_id") Long company_id,
														 @Param("drive_id") Long drive_id, @Param("status") String status);



	@Query(value = "SELECT u.username, u.email, r.apply_date, r.status, ru.company_name " +
			"FROM user u " +
			"JOIN recruitment_status r ON r.student_id = u.id " +
			"LEFT JOIN recruitment ru ON ru.placement_id = r.reqruitment_id "
			+"where u.id=:studentId"
			, nativeQuery = true)
	List<Map<String, Object>> findByStudentRecruitmentDetails(Long studentId);

	@Query(value = "SELECT u.username, u.email, r.apply_date, r.status, ru.company_name, ru.job_role, u.id, r.drive_id " +
			"FROM user u " +
			"JOIN recruitment_status r ON r.student_id = u.id " +
			"LEFT JOIN company_drive ru ON ru.drive_id = r.drive_id " +
			"WHERE r.company_id = :companyId and ru.job_role=:jobRole",
			nativeQuery = true)
	List<Map<String, Object>> findCompanyDriveApplyStudents(@Param("companyId")
															Long companyId,@Param("jobRole")String jobRole);


	@Query(value = "SELECT " +
			"cdl.manager_id AS manager_id, " +
			"(SELECT username FROM user WHERE id = cdl.manager_id) AS manager_name, " +
			"(SELECT email FROM user WHERE id = cdl.manager_id) AS manager_email, " +

			"cdl.internship_lead_id AS internship_lead_id, " +                                                                                                                                                                                                                                
			"(SELECT username FROM user WHERE id = cdl.internship_lead_id) AS lead_name, " +
			"(SELECT email FROM user WHERE id = cdl.internship_lead_id) AS lead_email, " +
			"cdl.company_id AS company_id " +
			"FROM comp_dept_link cdl " +
			"WHERE cdl.company_id = :company_id",
			nativeQuery = true)
	List<Map<String, Object>> findManagerAndLead(@Param("company_id") Long companyId);

	@Query("SELECT COUNT(u) FROM User u WHERE u.role = '10'")
    public Long findAllExternalStudentCount();
	
	@Query("SELECT COUNT(u) FROM User u WHERE u.role = '0'")
    public Long findAllInternalStudentCount();
	
	@Query("SELECT u.username, u.email, u.gender, u.contact_no, u.role FROM User u WHERE u.role = '20'")
	List<Object[]> findUsersWithRole20();
}
