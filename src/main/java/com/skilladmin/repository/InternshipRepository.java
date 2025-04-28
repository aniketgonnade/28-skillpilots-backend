package com.skilladmin.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.skilladmin.model.Internships;

public interface InternshipRepository extends JpaRepository<Internships, Long> {

	
	@Query(value="SELECT inter.* FROM internships2 inter,company_data2 "
			+ "cp WHERE cp.company_id=inter.company_id AND inter.student_id=:student_id AND inter.status=:status",nativeQuery = true)
    public ArrayList<Internships> getPastOrPendingOrCancelledInternshipsOfStudent(Long student_id,String status);

	
	@Query(value = "SELECT COUNT(*) FROM internships2 WHERE student_id = :studentId AND status IN (:statuses)", nativeQuery = true)
    Long countByStudentIdAndStatusInNative(Long studentId, List<String> statuses);
	

	@Query(value = "SELECT ir.student_id AS studentId, ir.company_id AS companyId, ir.college_id AS collegeId, " +
            "cd.company_name AS companyName, ir.technology, sdr.student_name AS studentName, " +
            "ir.duration, ir.date_started AS dateStarted, ir.date_completed AS dateCompleted " +
            "FROM internships2 ir " +
            "JOIN student_data sdr ON ir.student_id = sdr.student_id " +
            "JOIN company_data2 cd ON ir.company_id = cd.company_id " +
            "WHERE ir.student_id = :studentId and ir.date_completed IS NOT NULL",
            nativeQuery = true)
	List<Object[]> getCertificate(@Param("studentId") Long studentId);

	@Query(value="SELECT inter.* FROM internships2 inter,company_data2 "
			+ "cp WHERE cp.company_id=inter.company_id AND inter.student_id=:studentId ",nativeQuery = true)
	List<Internships> getDownoadurl(Long studentId);
	
	
	
	
	@Query(value = "SELECT u.username AS student_name, " +
            "i.company_name, " +
            "i.technology AS internship_title, " +
            "i.date_completed, " +
            "'completed' AS status " +
            "FROM internships2 i " +
            "JOIN user u ON i.student_id = u.id " +
            "WHERE i.status = 'Completed' " +
            "AND i.date_completed IS NOT NULL " +
            "ORDER BY i.date_completed DESC",
    nativeQuery = true)
List<Map<String, Object>> getInternshipCompletedList();
	
}
