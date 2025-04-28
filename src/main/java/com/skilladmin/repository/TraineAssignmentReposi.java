package com.skilladmin.repository;

import com.skilladmin.model.TraineeAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TraineAssignmentReposi extends JpaRepository<TraineeAssignment,Long> {
    //List<TraineeAssignment> findByBatchId(Long batchId);

    @Query(value = "SELECT * FROM trainee_assignment WHERE batch_id = :batchId", nativeQuery = true)
    List<TraineeAssignment> findAssignmentsByBatchId(@Param("batchId") Long batchId);

    @Query("select ta.assignMentName from TraineeAssignment ta where ta.id=:id")
    public  String getAssignmntName(Long id);


}
