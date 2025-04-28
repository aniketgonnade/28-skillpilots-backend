package com.skilladmin.serviceImpl;

import com.skilladmin.model.NotificationRequest;
import com.skilladmin.model.TraineeStudentMaterial;
import com.skilladmin.model.TrainingUsers;
import com.skilladmin.repository.NotificationRepository;
import com.skilladmin.repository.StudyMaterialRepository;
import com.skilladmin.repository.TrainingUserRepository;
import com.skilladmin.service.StudyMaterialService;
import com.skilladmin.util.FirebaseService;
import com.skilladmin.util.ProjectConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudyMateialServiceImpl implements StudyMaterialService {

    public  static final String UPLOAD_DIR="app/images";

    @Autowired
    private StudyMaterialRepository studyMaterialRepository;
    @Autowired
    private TrainingUserRepository trainingUserRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private FirebaseService firebaseService;

    @Override
    public TraineeStudentMaterial createStudyMatrial(TraineeStudentMaterial studentMaterial, MultipartFile file) throws IOException {
        NotificationRequest notificationRequest = new NotificationRequest();

        if (!file.isEmpty()) {
            // Ensure the directory for uploading files exists
            Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            // Get the original file name and ensure it has a PDF extension
            String fileName = file.getOriginalFilename();
            if (fileName != null && fileName.toLowerCase().endsWith(".pdf")) {
                Path filePath = uploadPath.resolve(fileName);
                // Save the PDF file to the server
                file.transferTo(filePath.toFile());

                // Set the file URL
                studentMaterial.setUploadUrl(fileName);

                // Check if batchId is present
                if (studentMaterial.getBatchId() != null) {
                    studentMaterial.setBatchId(studentMaterial.getBatchId());

                    // Fetch students based on batchId
                    List<TrainingUsers> students = trainingUserRepository.getStudents(ProjectConstants.trainee, studentMaterial.getBatchId());
                    System.out.println(students);

                    // Send notifications to students
                    for (TrainingUsers student : students) {
                        String notificationToken = student.getNotificationToken();
                        if (notificationToken != null && !notificationToken.isEmpty()) {
                            try {
                                notificationRequest.setDateTime(LocalDateTime.now());
                                notificationRequest.setMsgBody("New study material received!!");
                                notificationRequest.setTitle("Study Material Available!");
                                notificationRequest.setReceiverId(student.getStudentId());
                                notificationRepository.save(notificationRequest);

                                // Send notification via Firebase
                                firebaseService.sendNotification(notificationToken, "Study Material Available!!", "New study material received!");
                            } catch (Exception e) {
                                // Log the error, but don't stop the loop or throw an exception
                                System.out.println("Failed to send notification to student: " + student.getUsername() + " due to: " + e.getMessage());
                            }
                        }
                    }

                    // Save the study material and return it
                    return studyMaterialRepository.save(studentMaterial);
                } else {
                    throw new IOException("Batch ID is missing. Cannot assign study material without batch ID.");
                }
            } else {
                throw new IOException("Uploaded file is not a PDF");
            }
        } else {
            throw new IOException("No file uploaded");
        }
    }

    @Override
    public void deleteById(Long id) {
        studyMaterialRepository.deleteById(id);
    }

}



