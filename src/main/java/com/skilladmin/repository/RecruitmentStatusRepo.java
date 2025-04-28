package com.skilladmin.repository;

import com.skilladmin.dto.AppliedCompanyDTO;
import com.skilladmin.model.RecruitmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface RecruitmentStatusRepo extends JpaRepository<RecruitmentStatus,Long> {

//    @Query("SELECT new com.student.dto.RecruitmentStatusDto(u.username, u.email, r.applyDate, ru.companyName,ru.email) " +
//            "FROM User u " +
//            "JOIN RecruitmentStatus r ON r.studentId = u.id " +
//            "LEFT JOIN Recruitment ru ON ru.placementId = r.reqruitmentId " +
//            "WHERE u.id = :studentId")
//    List<RecruitmentStatusDto> findRecruitmentByStudentId(Long studentId);


//    @Query("SELECT r,s. FROM RecruitmentStatus r LEFT JOIN Recruitment s ON r.collegeId = s.collegeId AND s.studentId = :studentId WHERE r.collegeId = :collegeId")
//    List<RecruitmentStatus> findByCollegeAndStudent(@Param("collegeId") Long collegeId, @Param("studentId") Long studentId);

    RecruitmentStatus findByStudentIdAndReqruitmentId(Long studentId,Long reqruitmentId);
    RecruitmentStatus findByStudentIdAndDriveId(Long studentId,Long driveId);
    @Query("SELECT COALESCE(r.companyName, 'Unknown') AS companyName, " +
    	       "COALESCE(r.profile, 'Not Available') AS profile, " +
    	       "COALESCE(r.placementId, 0) AS placementId, " +
    	       "COALESCE(r.placementDetails.jobDescription, 'No Description') AS jobDescription, " +
    	       "COALESCE(r.placementDetails.city, 'Unknown') AS city, " +
    	       "COALESCE(r.ctc, 0) AS ctc, " +
    	       "COALESCE(s.status, 'Not Applied') AS status " +
    	       "FROM Recruitment r LEFT JOIN RecruitmentStatus s ON s.reqruitmentId = r.placementId " +
    	       "WHERE s.studentId = :studentId " +
    	       "ORDER BY s.statusId DESC")  // Replace `applicationDate` with your actual field for sorting
    	List<Map<String, Object>> getAppliedStatusForStudent(@Param("studentId") Long studentId);


    
    
    boolean existsByStudentIdAndReqruitmentId(Long studentId, Long reqruitmentId);
    
    
    @Query(value = "SELECT rs.status, " +
            "u.username AS student_name, " +
            "COALESCE(cd.job_role, r.profile) AS job_role, " +
            "COALESCE(r.company_name, cd.company_name) AS company_name, " +
            "rs.round_date AS round_date " +
            "FROM recruitment_status rs " +
            "JOIN user u ON rs.student_id = u.id " +
            "LEFT JOIN company_drive cd ON rs.reqruitment_id = cd.drive_id " +
            "LEFT JOIN recruitment r ON rs.reqruitment_id = r.placement_id " +
            "WHERE rs.status = 'SELECTED' " +
            "ORDER BY rs.round_date DESC", nativeQuery = true)
    public List<Map<String, Object>> findSelectedRoundsWithCompanyAndStudent();


	@Query("SELECT r FROM RecruitmentStatus r WHERE " +
			"CONCAT(r.roundDate, ' ', r.roundTime) BETWEEN :start AND :end")
	List<RecruitmentStatus> findByRoundDateTimeBetween(@Param("start") LocalDateTime start,
													   @Param("end") LocalDateTime end);

	@Query(value = "SELECT * FROM recruitment_status r WHERE " +
			"STR_TO_DATE(CONCAT(r.round_date, ' ', r.round_time), '%Y-%m-%d %H:%i') " +
			"BETWEEN :start AND :end ",
			nativeQuery = true)
	List<RecruitmentStatus> findDueInterviews(@Param("start") String start,
											  @Param("end") String end);



	@Query("SELECT new com.skilladmin.dto.AppliedCompanyDTO(c.companyName, c.jobRole, s.applyDate, s.status) " +
 	       "FROM CompanyDrive c LEFT JOIN RecruitmentStatus s ON s.driveId = c.driveId " +
 	       "WHERE s.studentId = :studentId ORDER BY s.driveId DESC")
 	List<AppliedCompanyDTO> getStudentRequestCompany(Long studentId);

	@Query("SELECT r FROM RecruitmentStatus r WHERE " +
			"CONCAT(r.roundDate, ' ', r.roundTime) = :targetDateTime")
	List<RecruitmentStatus> findByExactRoundTime(@Param("targetDateTime") String targetDateTime);

}
