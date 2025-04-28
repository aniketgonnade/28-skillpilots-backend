package com.skilladmin.repository;

import com.skilladmin.model.Recruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {

    @Query("SELECT p FROM Recruitment p WHERE CONCAT(',', p.departmentIds, ',') LIKE %:departmentId% and status='A'")
    List<Recruitment> findByDepartmentId(@Param("departmentId") String departmentId);

    List<Recruitment> findByStatusAndCollegeId(String status,Long collegeId);

    @Query("SELECT r.companyName,r.profile,r.placementId,r.placementDetails.jobDescription"
    		+ ",r.placementDetails.city,r.ctc,s.status,r.placementDetails.skills FROM Recruitment "
    		+ " r LEFT JOIN RecruitmentStatus s ON r.collegeId = s.collegeId AND s.studentId = :studentId WHERE r.collegeId = :collegeId and r.status='A'")
    List<Object[]> findByCollegeAndStudent(@Param("collegeId") Long collegeId, @Param("studentId") Long studentId);

    @Query("select r.companyName,r.profile,r.placementId,s.status from Recruitment r LEFT JOIN " +
            "RecruitmentStatus  s ON r.collegeId = s.collegeId where s.studentId=:studentId and s.collegeId=:collegeId")
    List<Object[]> getStudentAppliedHistory(Long collegeId,Long studentId);
    @Query("SELECT r FROM Recruitment r WHERE r.status = :status AND r.collegeId = :collegeId ORDER BY r.creationDate DESC")
    Page<Recruitment> findRecruitmentsByStatusAndCollegeId(@Param("status") String status, @Param("collegeId") Long collegeId, Pageable pageable) ;

}
