package com.skilladmin.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skilladmin.model.*;
import com.skilladmin.repository.*;
import com.skilladmin.service.AssigmentService;
import com.skilladmin.util.FirebaseService;
import com.skilladmin.util.ProjectConstants;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AssignmentServiceImpl implements AssigmentService {

    public static final String UPLOAD_DIR = "app/images";
//    public static final String UPLOAD_DIR;
//
//    static {
//        try {
//            UPLOAD_DIR = new ClassPathResource("static/images/").getFile().getAbsolutePath();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    public final String FILE_DIR =

    public static final String UPLOAD_ = "app/images/";


    @Autowired
    private TraineAssignmentReposi traineAssignmentReposi;
    @Autowired
    private BatchRepository batchRepository;
    @Autowired
    private BannerRepository bannerRepository;
    @Autowired
    private TrainingUserRepository trainingUserRepository;
    @Autowired
    private StudentAssignmentRepository assignmentRepository;
    @Autowired
    private FirebaseService firebaseService;
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public TraineeAssignment addAssignment(TraineeAssignment traineeAssignment, MultipartFile pdfFile) throws IOException {
        if (!pdfFile.isEmpty() || pdfFile != null) {
            // Ensure the directory for uploading files exists
            Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            // Get the original file name and ensure it has a PDF extension
            String fileName = pdfFile.getOriginalFilename();
            if (fileName != null && fileName.toLowerCase().endsWith(".pdf")) {
                Path filePath = uploadPath.resolve(fileName);

                // Save the PDF file to the server
                pdfFile.transferTo(filePath.toFile());

                // Construct the full URL to the PDF
                //   String pdfUrl = "https://shubhamkohad.site/files/" + fileName;
                String pdfUrl = "/images/" + fileName;
                // Save the PDF path as a URL
                traineeAssignment.setFile(fileName);


                return traineAssignmentReposi.save(traineeAssignment);

            } else {
                return traineAssignmentReposi.save(traineeAssignment);
            }
        }

        return null;
    }

    @Override
    public Batch assignAssignMentToBach(Long batchId, Long assignMentId) {
        NotificationRequest notificationRequest = new NotificationRequest();
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found"));

        TraineeAssignment newAssignment = traineAssignmentReposi.findById(assignMentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        List<TrainingUsers> objects = trainingUserRepository.getStudents(ProjectConstants.trainee, batchId);
        System.out.println(objects);
        for (TrainingUsers student : objects) {
            String notificationToken = student.getNotificationToken();
            if (notificationToken != null && !notificationToken.isEmpty()) {
                try {
                    notificationRequest.setDateTime(LocalDateTime.now());
                    notificationRequest.setMsgBody(" You have a new assignment!");
                    notificationRequest.setTitle("Assignment Assigned!");
                    notificationRequest.setReceiverId(student.getStudentId());
                    notificationRepository.save(notificationRequest);
                    firebaseService.sendNotification(notificationToken, "Assignment Assigned!", " You have a new assignment!");
                } catch (Exception e) {
                    // Log the error, but don't stop the loop or throw an exception
                    System.out.println("Failed to send notification to student: " + student.getUsername() + " due to: " + e.getMessage());
                }
            }
        }
        // Add new assignment to the batch
        batch.getTraineeAssignments().add(newAssignment);
        newAssignment.setBatch(batch);

        // Save both entities
        traineAssignmentReposi.save(newAssignment);
        return batchRepository.save(batch);

    }

    @Override
    public List<TraineeAssignment> getAssignmentByBatchId(Long batchId) {
        return traineAssignmentReposi.findAssignmentsByBatchId(batchId);
    }

    @Override
    public Poster addBanner(MultipartFile file) throws IOException {
        Poster banner = new Poster();
        if (!file.isEmpty()) {
            Path uploadPath = Paths.get(UPLOAD_).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);
            String fileName = file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            file.transferTo(filePath.toFile()); //save file to the server
            String imageUrl = "/images/" + fileName;
            banner.setImagePath(imageUrl);
        }
        return bannerRepository.save(banner);
    }

    @Override
    public List<Poster> getAllBaner() {
        return bannerRepository.findAll();
    }

    @Override
    public StudentAssignment uploadSolvedAssinment(Long studentId, Long batchId, Long assignMentId, MultipartFile pdfFile,String description) throws IOException {
        StudentAssignment studentAssignment = new StudentAssignment();

        if (!pdfFile.isEmpty() || pdfFile != null) {
            // Ensure the directory for uploading files exists
            Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            // Get the original file name and ensure it has a PDF extension
            String fileName = pdfFile.getOriginalFilename();
            if (fileName != null && fileName.toLowerCase().endsWith(".pdf") || fileName.toLowerCase().endsWith(".jpg") || fileName.toLowerCase().endsWith(".png")) {
                Path filePath = uploadPath.resolve(fileName);

                // Save the PDF file to the server
                pdfFile.transferTo(filePath.toFile());

                // Construct the full URL to the PDF
                //   String pdfUrl = "https://shubhamkohad.site/files/" + fileName;

                // Save the PDF path as a URL
                TraineeAssignment assignment = traineAssignmentReposi.findById(assignMentId)
                        .orElseThrow(() -> new RuntimeException("Assignment not found"));
                studentAssignment.setSolvedFile(fileName); // Assuming `pdfPath` is the correct field in your Product entity
                studentAssignment.setUploadDate(LocalDate.now());
                studentAssignment.setStudentId(studentId);
                studentAssignment.setBatchId(batchId);
                studentAssignment.setAssignment(assignment);
                studentAssignment.setDescription(description);
                return assignmentRepository.save(studentAssignment);

            } else {
                throw new RuntimeException("pdf not saved");
            }

        }
        return studentAssignment;
    }

//    @Override
//    public List<Map<String, Object>> getStudentAssignmentWithTrainingUser(Long batchId) {
//        List<Object[]> resultList = assignmentRepository.findStudentAssignmentWithTrainingUser(batchId);
//        List<Map<String, Object>> responseList = new ArrayList<>();
//
//        for (Object[] result : resultList) {
//            Map<String, Object> map = new HashMap<>();
//
//            String trainingUserName = (String) result[2]; // Assuming result[1] is the name from training_users
//            String name = (String) result[6];
//           Long id = (Long) result[5];
//            Date date= (Date) result[4];
//         String assignmentName=   traineAssignmentReposi.getAssignmntName(id);
////            ObjectMapper mapper = new ObjectMapper();
////            String json = null;
////            try {
////                json = mapper.writeValueAsString(assignment);
////            } catch (JsonProcessingException e) {
////                throw new RuntimeException(e);
////            }
//           map.put("assignmentName", assignmentName);
//            map.put("studentAssignment", trainingUserName);
//            map.put("trainingUserName", name);
//            map.put("date",date);
//
//
//            responseList.add(map);
//        }
//
//        return responseList;
//    }
    @Override
public List<Map<String, Object>> getStudentAssignmentWithTrainingUser(Long batchId) {
    // Fetching result as a list of maps
    List<Map<String, Object>> resultList = assignmentRepository.findStudentAssignmentWithTrainingUser(batchId);
    List<Map<String, Object>> responseList = new ArrayList<>();
    // Iterating over the list of maps
    for (Map<String, Object> result : resultList) {
        Map<String, Object> map = new HashMap<>();
        // Extracting values using column names instead of indexes
        String trainingUserName = (String) result.get("trainingUserName");
        String name = (String) result.get("solved_file");
        Long id = (Long) result.get("assignment_id");
        Date date = (Date) result.get("upload_date");
        // Fetching assignment name using the ID
        String assignmentName = traineAssignmentReposi.getAssignmntName(id);
        // Populating the map with the required values
        map.put("assignmentName", assignmentName);
        map.put("studentAssignment",name );
        map.put("trainingUserName", trainingUserName);
        map.put("date", date);
        // Adding the map to the response list
        responseList.add(map);
    }
    return responseList;
}

    @Override
    public List<StudentAssignment> getStudentAssignmentForStudent(Long studentId) {
        return assignmentRepository.findByStudentId(studentId);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        assignmentRepository.deleteByAssignmentId(id);

        // Then delete the trainee assignment
        traineAssignmentReposi.deleteById(id);
    }

    @Override
    public void deleteSolveAssignment(Long id) {
        assignmentRepository.deleteById(id);
    }
}