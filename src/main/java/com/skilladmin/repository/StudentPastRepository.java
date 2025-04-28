package com.skilladmin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.skilladmin.model.StudentPast;


public interface StudentPastRepository extends JpaRepository<StudentPast, Long> {
	@Query("Select sp from StudentPast sp where sp.student_id=:studentId and sp.stud_past_id=:studpastId")
	public StudentPast editStudentPast(Long studentId,Long studpastId);

	@Query("select sp,s.curr_year from StudentPast sp left join Student s on s.student_id=sp.student_id where s.student_id=:studentId")
	List<Object[]> editEducation(@Param("studentId") Long studentId);


//	@Query(value = """
//        SELECT
//            ts.student_id AS studentId,
//            ts.student_name AS studentName,
//
//            MAX(CASE WHEN sd.category = 'SSC' AND CAST(sd.description AS UNSIGNED) >= :sscThreshold THEN sd.description END) AS sscDescription,
//            MAX(CASE WHEN sd.category = 'HSC' AND CAST(sd.description AS UNSIGNED) >= :hscThreshold THEN sd.description END) AS hscDescription,
//            MAX(CASE WHEN sd.category = 'UG' AND CAST(sd.description AS UNSIGNED) >= :ugThreshold THEN sd.description END) AS ugDescription,
//            rs.status AS status
//        FROM student_data ts
//        LEFT JOIN students_past sd ON ts.student_id = sd.student_id
//        LEFT JOIN recruitment_status rs ON rs.student_id = ts.student_id AND (rs.reqruitment_id = :placementId OR rs.reqruitment_id IS NULL)
//        WHERE ts.college_id = :collegeId
//        GROUP BY ts.student_id, ts.student_name, rs.status
//        HAVING sscDescription IS NOT NULL AND hscDescription IS NOT NULL AND ugDescription IS NOT NULL
//        """, nativeQuery = true)
//	List<Object[]> findStudentPerformances(@Param("sscThreshold") String sscThreshold,
//										   @Param("hscThreshold") String hscThreshold,
//										   @Param("ugThreshold") String ugThreshold,
//										   @Param("collegeId") Long collegeId,
//										   @Param("placementId") Long placementId);


//*********************************************************//  25/11
//	@Query(value = """
//        SELECT
//            ts.student_id AS studentId,
//            ts.student_name AS studentName,
//
//            MAX(CASE WHEN sd.category = 'SSC' AND CAST(sd.description AS UNSIGNED) >= :sscThreshold THEN sd.description END) AS sscDescription,
//            MAX(CASE WHEN sd.category = 'HSC' AND CAST(sd.description AS UNSIGNED) >= :hscThreshold THEN sd.description END) AS hscDescription,
//			MAX(CASE WHEN sd.category = 'UG' AND CAST(sd.description AS UNSIGNED) >= :ugThreshold AND sd.end_date = :passYear THEN sd.description END) AS ugDescription,
//			                                             rs.status AS status
//        FROM student_data ts
//        LEFT JOIN students_past sd ON ts.student_id = sd.student_id
//        LEFT JOIN recruitment_status rs ON rs.student_id = ts.student_id AND (rs.reqruitment_id = :placementId OR rs.reqruitment_id IS NULL)
//        WHERE ts.college_id = :collegeId
//        GROUP BY ts.student_id, ts.student_name, rs.status
//        HAVING sscDescription IS NOT NULL AND hscDescription IS NOT NULL AND ugDescription IS NOT NULL
//        """, nativeQuery = true)
//	List<Object[]> findStudentPerformances(@Param("sscThreshold") String sscThreshold,
//										   @Param("hscThreshold") String hscThreshold,
//										   @Param("ugThreshold") String ugThreshold,
//										   @Param("collegeId") Long collegeId,
//										   @Param("placementId") Long placementId,@Param("passYear") String passYear);

	//*************************************************************************//

	@Query(value = """
        SELECT 
            ts.student_id AS studentId, 
            ts.student_name AS studentName,
             
            MAX(CASE 
                WHEN sd.category = 'SSC' AND (CAST(REPLACE(sd.description, '.', '') AS UNSIGNED)) >= :sscThreshold THEN sd.description 
            END) AS sscDescription,
            MAX(CASE 
                WHEN sd.category = 'HSC' AND (CAST(REPLACE(sd.description, '.', '') AS UNSIGNED)) >= :hscThreshold THEN sd.description 
            END) AS hscDescription,
            MAX(CASE 
                WHEN sd.category = 'UG' AND (CAST(REPLACE(sd.description, '.', '') AS UNSIGNED)) >= :ugThreshold AND sd.end_date = :passYear THEN sd.description 
            END) AS ugDescription,
            rs.status AS status
        FROM student_data ts 
        LEFT JOIN students_past sd ON ts.student_id = sd.student_id 
        LEFT JOIN recruitment_status rs ON rs.student_id = ts.student_id AND (rs.reqruitment_id = :placementId OR rs.reqruitment_id IS NULL)
        WHERE ts.college_id = :collegeId
        GROUP BY ts.student_id, ts.student_name, rs.status
        HAVING sscDescription IS NOT NULL AND hscDescription IS NOT NULL AND ugDescription IS NOT NULL
        """, nativeQuery = true)
	List<Object[]> findStudentPerformances(@Param("sscThreshold") String sscThreshold,
										   @Param("hscThreshold") String hscThreshold,
										   @Param("ugThreshold") String ugThreshold,
										   @Param("collegeId") Long collegeId,
										   @Param("placementId") Long placementId,
										   @Param("passYear") String passYear);




//old
//	@Query(value = "SELECT " +
//			"    ts.student_id AS studentId, " +
//			"    ts.student_name AS studentName, " +
//			"    MAX(CASE WHEN sd.category = 'SSC' AND CAST(sd.description AS UNSIGNED) >= :hsc THEN sd.description END) AS sscDescription, " +
//			"    MAX(CASE WHEN sd.category = 'HSC' AND CAST(sd.description AS UNSIGNED) >= :ssc THEN sd.description END) AS hscDescription, " +
//			"    MAX(CASE WHEN sd.category = 'UG' AND CAST(sd.description AS UNSIGNED) >= :ug " +
//			"              AND sd.end_date IN :years THEN sd.description END) AS ugDescription, " +
//			"    rs.status AS status " +
//			"FROM student_data ts " +
//			"LEFT JOIN students_past sd ON ts.student_id = sd.student_id " +
//			"LEFT JOIN recruitment_status rs ON rs.student_id = ts.student_id AND (rs.reqruitment_id = :placementId OR rs.reqruitment_id IS NULL) " +
//			"WHERE ts.college_id = :collegeId " +
//			"GROUP BY ts.student_id, ts.student_name, rs.status " +
//			"HAVING sscDescription IS NOT NULL AND hscDescription IS NOT NULL AND ugDescription IS NOT NULL",nativeQuery = true)
//			List<Object[]> findStudentsByCriteria(@Param("ssc") String ssc,
//			@Param("hsc") String hsc,
//			@Param("ug") String ug,@Param("collegeId")Long collegeId, @Param("placementId") Long placementId,
//												  @Param("years") List<String> years);

	@Query(value = "SELECT " +
			"    ts.student_id AS studentId, " +
			"    ts.student_name AS studentName, " +
			"    MAX(CASE WHEN sd.category = 'SSC' AND CAST(sd.description AS UNSIGNED) >= :ssc THEN sd.description END) AS sscDescription, " +
			"    MAX(CASE WHEN sd.category = 'HSC' AND CAST(sd.description AS UNSIGNED) >= :hsc THEN sd.description END) AS hscDescription, " +
			"    MAX(CASE WHEN sd.category = 'UG' AND CAST(sd.description AS UNSIGNED) >= :ug " +
			"              AND sd.end_date IN :years THEN sd.description END) AS ugDescription " +
			"FROM student_data ts " +
			"LEFT JOIN students_past sd ON ts.student_id = sd.student_id " +
			"WHERE ts.college_id = :collegeId " +
			"GROUP BY ts.student_id, ts.student_name " +
			"HAVING sscDescription IS NOT NULL AND hscDescription IS NOT NULL AND ugDescription IS NOT NULL", nativeQuery = true)
	List<Object[]> findStudentsByCriteria(@Param("ssc") String ssc,
										  @Param("hsc") String hsc,
										  @Param("ug") String ug,
										  @Param("collegeId") Long collegeId,
										  @Param("years") List<String> years);







//	@Query(value = "SELECT " +
//			"    ts.student_id AS studentId, " +
//			"    ts.student_name AS studentName, " +
//			"    MAX(CASE WHEN sd.category = 'SSC' AND CAST(sd.description AS UNSIGNED) >= :hsc THEN sd.description END) AS sscDescription, " +
//			"    MAX(CASE WHEN sd.category = 'HSC' AND CAST(sd.description AS UNSIGNED) >= :ssc THEN sd.description END) AS hscDescription, " +
//			"    MAX(CASE WHEN sd.category = 'UG' AND CAST(sd.description AS UNSIGNED) >= :ug " +
//			"              AND sd.end_date IN :years THEN sd.description END) AS ugDescription, " +
//			"    rs.status AS status, " +  // Comma added here
//			"    sd.end_date AS passYear " +
//			"FROM student_data ts " +
//			"LEFT JOIN students_past sd ON ts.student_id = sd.student_id " +
//			"LEFT JOIN recruitment_status rs ON rs.student_id = ts.student_id AND (rs.reqruitment_id = :placementId OR rs.reqruitment_id IS NULL) " +
//			"WHERE ts.college_id = :collegeId " +
//			"GROUP BY ts.student_id, ts.student_name, rs.status, sd.end_date " +
//			"HAVING sscDescription IS NOT NULL AND hscDescription IS NOT NULL AND ugDescription IS NOT NULL",
//			nativeQuery = true)
//	List<Object[]> findStudentsByCriteria(@Param("ssc") String ssc,
//										  @Param("hsc") String hsc,
//										  @Param("ug") String ug,
//										  @Param("collegeId") Long collegeId,
//										  @Param("placementId") Long placementId,
//										  @Param("years") List<String> years);
	}