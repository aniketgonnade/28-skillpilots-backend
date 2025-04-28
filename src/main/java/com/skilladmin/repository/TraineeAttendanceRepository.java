package com.skilladmin.repository;

import com.skilladmin.model.TraineeAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TraineeAttendanceRepository extends JpaRepository<TraineeAttendance,Long> {

    @Query(value = "select a.* from trainee_attendance a where a.student_id=:studentId",nativeQuery = true)
    public List<TraineeAttendance> getStudentAttendence(@Param("studentId") Long studentId );
}
