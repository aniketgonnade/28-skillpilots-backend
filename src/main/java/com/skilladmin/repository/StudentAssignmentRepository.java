package com.skilladmin.repository;

import com.skilladmin.model.StudentAssignment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface StudentAssignmentRepository extends JpaRepository<StudentAssignment,Long> {


//    @Query(value = "SELECT tu.*, sa.name FROM student_assignment tu " +
//            "LEFT JOIN training_users sa ON tu.student_id = sa.student_id " +
//            "WHERE tu.batch_id = :batchId", nativeQuery = true)
//    List<Object[]> findStudentAssignmentWithTrainingUser(@Param("batchId") Long batchId);

    @Query(value = "SELECT tu.*, sa.name as trainingUserName " +
            "FROM student_assignment tu " +
            "LEFT JOIN training_users sa ON tu.student_id = sa.student_id " +
            "WHERE tu.batch_id = :batchId", nativeQuery = true)
    List<Map<String, Object>> findStudentAssignmentWithTrainingUser(@Param("batchId") Long batchId);
    List<StudentAssignment> findByBatchId(Long batchId);
    List<StudentAssignment> findByStudentId(Long studentId);

    @Modifying
    @Transactional
    @Query("DELETE FROM StudentAssignment sa WHERE sa.assignment.id = :assignmentId")
    void deleteByAssignmentId(@Param("assignmentId") Long assignmentId);
}
