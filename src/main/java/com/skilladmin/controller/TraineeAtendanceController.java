package com.skilladmin.controller;

import com.skilladmin.model.TraineeAttendance;
import com.skilladmin.service.TraineeAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = {"*"})

public class TraineeAtendanceController {

    @Autowired
    private TraineeAttendanceService attendanceService;


    @PostMapping("/markAttendance")
    public ResponseEntity<List<Map<String, Object>>> markAttendanceForStudents(
            @RequestParam String date,@RequestParam Long batchId,
            @RequestBody Map<String, Boolean> studentStatuses) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate localDate = LocalDate.parse(date, formatter);
        List<Map<String, Object>> attendances = attendanceService.markAttendanceForStudents(date,batchId, studentStatuses);
        return ResponseEntity.ok(attendances);
    }

    @GetMapping("/getAttendance")
    public ResponseEntity<Object> getAttendance(@RequestParam Long studentId) {
        List<TraineeAttendance> attendances = attendanceService.getStudentAttendence(studentId);
        if (!attendances.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("msg", "studens get Succesfully",
                    "attendances", attendances, "status", "200"));
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("msg", "Not found"));
        }
    }



    @GetMapping("/getAttendanceByBatch")
    public List<Map<String, Object>> getStatusAndNameByBatchId(Long batchId,String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate =  LocalDate.parse(date,formatter);
        List<Object[]> results = attendanceService.findStatusAndNameByBatchId(batchId, localDate);

        return results.stream()
                .map(result -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", result[1]);
                    map.put("status", result[0]);
                    return map;
                })
                .distinct()
                .collect(Collectors.toList());
    }

}
