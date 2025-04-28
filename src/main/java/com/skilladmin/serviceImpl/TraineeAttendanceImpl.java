package com.skilladmin.serviceImpl;

import com.skilladmin.model.NotificationRequest;
import com.skilladmin.model.TraineeAttendance;
import com.skilladmin.model.TrainingUsers;
import com.skilladmin.repository.NotificationRepository;
import com.skilladmin.repository.TraineeAttendanceRepository;
import com.skilladmin.repository.TrainingUserRepository;
import com.skilladmin.service.TraineeAttendanceService;
import com.skilladmin.util.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TraineeAttendanceImpl implements TraineeAttendanceService {
    @Autowired
    private TrainingUserRepository trainingUserRepository;

    @Autowired
    private TraineeAttendanceRepository traineeAttendanceRepository;
    @Autowired
    private FirebaseService firebaseService;
    @Autowired
    private NotificationRepository notificationRepository;
    @Override
    public List<Map<String, Object>> markAttendanceForStudents(String date,Long batchId, Map<String, Boolean> studentStatuses) {
        List<Map<String, Object>> attendances = new ArrayList<>();

        for (Map.Entry<String, Boolean> entry : studentStatuses.entrySet()) {
            String id = entry.getKey();
            Long studentId = Long.parseLong(id);
            boolean status = entry.getValue();

            // Fetch the student
            TrainingUsers student = trainingUserRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            // Create a new attendance record
            TraineeAttendance attendance = new TraineeAttendance();
            attendance.setTrainingUsers(student);
            attendance.setLocalDateTime(date);
            attendance.setStatus(status);
            attendance.setBatchId(batchId);

            // Save the attendance record
            traineeAttendanceRepository.save(attendance);
            NotificationRequest notificationRequest = new NotificationRequest();

            // Check if the student has a notification token and send notification if present
            String notificationToken = student.getNotificationToken();
            if (notificationToken != null && !notificationToken.isEmpty()) {
                try {
                    String message = status ? "You are marked Present for the day!" : "You are marked Absent for the day!";

                    notificationRequest.setDateTime(LocalDateTime.now());
                    notificationRequest.setMsgBody(message);
                    notificationRequest.setTitle("Attendance Marked");
                    notificationRequest.setReceiverId(student.getStudentId());
                    notificationRepository.save(notificationRequest);
                    // Customize the notification message based on the attendance status (boolean)
                    firebaseService.sendNotification(notificationToken, "Attendance Marked", message);
                } catch (Exception e) {
                    // Log any error during notification sending
                    System.out.println("Failed to send notification to student: " + student.getUsername() + " due to: " + e.getMessage());
                }
            }

            // Create a response map with the attendance details
            Map<String, Object> response = new HashMap<>();
            response.put("studentId", studentId);
            response.put("attendance", attendance);
            attendances.add(response);
        }

        return attendances;
    }


    @Override
    public List<TraineeAttendance> getStudentAttendence(Long studentId) {
        return traineeAttendanceRepository.getStudentAttendence(studentId);
    }
    @Override
    public List<Object[]> findStatusAndNameByBatchId(Long batchId, LocalDate localDate) {
        return trainingUserRepository.findStatusAndNameByBatchId(batchId,localDate,"isTrainee");
    }
}
