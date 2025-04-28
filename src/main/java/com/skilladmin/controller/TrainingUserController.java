package com.skilladmin.controller;

import com.skilladmin.dto.LoginRequest;
import com.skilladmin.dto.TraineeStudentRegistration;
import com.skilladmin.dto.TraineeUpdateDto;
import com.skilladmin.model.TrainingUsers;
import com.skilladmin.model.User;
import com.skilladmin.repository.TrainingUserRepository;
import com.skilladmin.service.TrainingUserService;
import com.skilladmin.util.ProjectConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"*"})
@RestController
public class TrainingUserController {
    @Autowired
    private TrainingUserService trainingUserService;
    @Autowired
    private TrainingUserRepository trainingUserRepository;

    @PostMapping("/createTraineeStudent")
    public ResponseEntity<Object> createTraineeStudent(@RequestBody TraineeStudentRegistration registration) {
        try {
            // Call the service method to create the trainee student
            TrainingUsers trainingUsers = trainingUserService.craeteTraingStudents(registration);

            // Return success response with the saved training user
            return ResponseEntity.status(HttpStatus.CREATED).body(trainingUsers);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            // For any other exceptions, return an internal server error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/traineeStudents")
    public ResponseEntity<?> getAllTraineeStudents() {
        List<Object[]> trainingUsersList = trainingUserService.getAllTraiStudents();
        if (!trainingUsersList.isEmpty()) {
            // Create list of trainee students mapped to name and status
            List<Map<String, Object>> traineeStudents = trainingUsersList.stream()
                    .map(result -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("batchname", result[1]);
                        map.put("name", result[0]);
                        return map;
                    })
                    .distinct() // Remove duplicates
                    .collect(Collectors.toList());

            return ResponseEntity.ok(traineeStudents); // Return as List
        } else {
            // Return status 204 if no content is found
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(Map.of("msg", "Not found", "status", "404"));
        }
    }


    @PostMapping("/logIn")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Call the login method from the service
            LoginRequest response = trainingUserService.login(loginRequest);

            // If login is successful, return OK with the loginRequest object
            if ("200".equals(response.getStatus())) {
                return ResponseEntity.ok(response); // HTTP 200 OK
            }

            // If the login fails (user not found or invalid password), return 400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // HTTP 400 Bad Request

        } catch (Exception e) {
            // Handle any unexpected exceptions
            LoginRequest errorResponse = new LoginRequest();
            errorResponse.setMsg("An unexpected error occurred: " + e.getMessage());
            errorResponse.setStatus("500");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse); // HTTP 500 Internal Server Error
        }
    }
    @GetMapping("/students")
    public ResponseEntity<Object> getStudentsByBatch(@RequestParam Long batchId) {
        List<TrainingUsers> trainingUsersList = trainingUserService.getStudensBatchVice(batchId);

        if (!trainingUsersList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("msg", "studens get Succesfully",
                    "students", trainingUsersList, "status", "200"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("msg", "Not found", "status", "404"));
        }
    }

    @PostMapping("/addManager")
    public ResponseEntity<Object> createManager(@RequestBody TrainingUsers trainingUsers) {

        TrainingUsers trainingUsers1 = trainingUserService.addManager(trainingUsers);
        return ResponseEntity.status(HttpStatus.OK).body(trainingUsers1);
    }

    @GetMapping("/getManager")
    public ResponseEntity<Object> getManager(@RequestParam Long id) {
        TrainingUsers trainingUsers = trainingUserService.getTrainingUserById(id);
        if (trainingUsers != null) {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("msg", "found", "manager", trainingUsers));
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("msg", "Not found"));
        }
    }

    @PostMapping("/uploadImage")
    public ResponseEntity<Object> uploadImageManager(@RequestParam Long id,
                                                     @RequestParam("file") MultipartFile file) {
        try {
            TrainingUsers trainingUsers = trainingUserService.uploadPhotoManager(id, file);
            return ResponseEntity.status(200).body(Map.of("msg", "Image uploaded successfully",
                    "imagePath", trainingUsers.getImagePath()));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("msg", "Image upload failed",
                    "error", e.getMessage()));
        }
    }


    @GetMapping("/allUsers")
    public ResponseEntity<Object> getAllUser() {
        List<TrainingUsers> trainingUsers = trainingUserService.getAllUser();
        if (!trainingUsers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("msg", "Users get Succesfully",
                    "students", trainingUsers, "status", "200"));
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("msg", "Not found"));

        }
    }

    @PutMapping("/updateTrainee")
    public ResponseEntity<Object> updateStudent(@RequestParam Long studentId, @RequestBody TraineeUpdateDto trainingUsers) {
        try {
            TrainingUsers users = trainingUserService.updateStudent(studentId, trainingUsers);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("msg", "Users Updte Succesfully",
                    "students", users, "status", "200"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("msg", "Users Not update Succesfully",
                    "status", e.getMessage()));
        }

    }

    @DeleteMapping("/deleteStudent")
    public ResponseEntity<Object> deleteStudent(@RequestParam Long studentId) {
        try {
            trainingUserService.deleteStudent(studentId);
            return ResponseEntity.ok("Student with ID " + studentId + " has been deleted successfully.");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student with ID " + studentId + " not found.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the student.");
        }
    }

    @GetMapping("/findStudents")
    public ResponseEntity<Object> getStudents() {
        Map<String, Object> map = new HashMap<>();

        List<Object[]> objects = trainingUserRepository.findByRole(ProjectConstants.trainee);

        map.put("name", objects.get(0));
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("msg", "Users get Succesfully",
                "students", objects, "status", "200"));
    }


    @PutMapping("/deactivate")
    public ResponseEntity<Object> deactivateUser(@RequestParam Long userId) {
        TrainingUsers trainingUsers = trainingUserService.deactivateUser(userId);

        String message = trainingUsers.isStatus() ? "User Activated Successfully" : "User Deactivated Successfully";

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("msg", message, "user", trainingUsers));
    }

    @GetMapping("/getbyStatus")
    public ResponseEntity<List<TrainingUsers>> getByStatus() {
        try {
            List<TrainingUsers> users = trainingUserService.findByStatus(false);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while fetching users by status.");
        }
    }

    @GetMapping("/getById")
    public ResponseEntity<TrainingUsers> getById(@RequestParam Long id) {
        try {
            TrainingUsers user = trainingUserService.getTrainId(id)
                    .orElseThrow(() -> new RuntimeException("TrainingUser with ID " + id + " not found"));
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            throw e; // This will be handled by the global exception handler
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred");
        }
    }

    @GetMapping("/studentBatch")
    public ResponseEntity<Object> getStudentBatch(@RequestParam Long id) {
        try {
            Object[] user = trainingUserService.getStudentsWithBatch(id);



            return ResponseEntity.status(HttpStatus.OK).body(Map.of("msg", "Student get", "student", user)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("msg", "Student Not found"));

        }
    }
}
