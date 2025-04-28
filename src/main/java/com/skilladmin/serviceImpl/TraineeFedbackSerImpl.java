package com.skilladmin.serviceImpl;

import com.skilladmin.model.NotificationRequest;
import com.skilladmin.model.TraineeFeedback;
import com.skilladmin.model.TrainingUsers;
import com.skilladmin.repository.NotificationRepository;
import com.skilladmin.repository.TrainingUserRepository;
import com.skilladmin.repository.TraneeFeedbackRepository;
import com.skilladmin.service.TraineeFeedBackService;
import com.skilladmin.util.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TraineeFedbackSerImpl implements TraineeFeedBackService {

    @Autowired
    private TraneeFeedbackRepository  feedbackRepository;
    @Autowired
    private FirebaseService firebaseService;
    @Autowired
    private TrainingUserRepository trainingUserRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public TraineeFeedback giveFeedBack(Long studentId, TraineeFeedback traineeFeedback) {
      TrainingUsers  student= trainingUserRepository.findById(studentId).orElseThrow(()->new RuntimeException("Student not found"+studentId));
        traineeFeedback.setStudentId(studentId);
        NotificationRequest notificationRequest = new NotificationRequest();

        String notificationToken = student.getNotificationToken();
        if (notificationToken != null && !notificationToken.isEmpty()) {
            try {

                notificationRequest.setDateTime(LocalDateTime.now());
                notificationRequest.setMsgBody("📋 New Weekly Feedback!");
                notificationRequest.setTitle("You have received new feedback for this week. Please check it out and provide your response!");
                notificationRequest.setReceiverId(student.getStudentId());
                notificationRepository.save(notificationRequest);
                firebaseService.sendNotification(notificationToken,
                        "📋 New Weekly Feedback!",
                        "You have received new feedback for this week. Please check it out and provide your response!"
                );            }
            catch (Exception e) {
                // Log the error, but don't stop the loop or throw an exception
                System.out.println("Failed to send notification to student: " + student.getUsername() + " due to: " + e.getMessage());
            }
        }
        return feedbackRepository.save(traineeFeedback);
    }

    @Override
    public List<TraineeFeedback> getStudentFeedback(Long studentId) {
        return feedbackRepository.findByStudentId(studentId);
    }
}
