package com.skilladmin.service;

import com.skilladmin.model.TraineeAttendance;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface TraineeAttendanceService {
    public List<Map<String, Object>> markAttendanceForStudents(String date,Long batchId, Map<String, Boolean> studentStatuses) ;

    public List<TraineeAttendance> getStudentAttendence( Long studentId );
    List<Object[]> findStatusAndNameByBatchId(Long batchId,
                                              LocalDate localDate
                                              );
}
