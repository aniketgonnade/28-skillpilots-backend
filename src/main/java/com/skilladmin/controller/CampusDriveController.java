package com.skilladmin.controller;

import com.skilladmin.model.CampusDrive;
import com.skilladmin.model.CampusResultRepository;
import com.skilladmin.repository.CampusDriveRepository;
import com.skilladmin.service.CampusDriveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin(origins = {"*"})

public class CampusDriveController {

    @Autowired
    private CampusDriveService campusDriveService;
    @Autowired
    private CampusDriveRepository campusDriveRepository;
    @Autowired
    private CampusResultRepository campusResultRepository;

    @PostMapping("/registerDrive")
    public ResponseEntity<Object> registerCampusDrive(@RequestBody CampusDrive campusDrive) {
        HashMap<String, Object> response = new HashMap<>();

        try {
            CampusDrive savedCampusDrive = campusDriveService.register(campusDrive);
            response.put("statusCode", 200);
            response.put("message", "Campus Drive registered successfully.");
            response.put("data", savedCampusDrive); // Returns the saved data
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("statusCode", 500);
            response.put("message", "Campus Drive registration failed.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/loginDrive")
    public ResponseEntity<Object> login(@RequestParam String email, @RequestParam String password) {
        HashMap<String, Object> response = new HashMap<>();

        try {
            // Find user by email
            CampusDrive user = campusDriveRepository.findByEmailId(email);

            if (user != null && user.getPassword().equals(password)) {
                // Check if the user is eligible for re-exam (status = 1L)
                if (user.getStatus() == 1L) {
                    // Login successful if status is 1
                    response.put("statusCode", 200);
                    response.put("message", "Login successful.");
                    response.put("userData", user);  // Returning the user data (customize as needed)
                    return new ResponseEntity<>(response, HttpStatus.OK);
                } else {
                    // Status is not 1, user not eligible for re-exam
                    response.put("statusCode", 403);  // Forbidden status code
                    response.put("message", "Not eligible for re-exam.");
                    return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
                }
            } else {
                // Email or password is incorrect
                response.put("statusCode", 401);
                response.put("message", "Invalid email or password.");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            // General error handling
            response.put("statusCode", 500);
            response.put("message", "Login failed. Please try again.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/passedStudents")
    public ResponseEntity<Object> getPassedStudents() {
        HashMap<String, Object> response = new HashMap<>();

        try {
            // Fetch the names of students who passed
            List<Object> passedStudents = campusResultRepository.getCampusResult();

            if (!passedStudents.isEmpty()) {
                response.put("statusCode", 200);
                response.put("message", "Passed students retrieved successfully.");
                response.put("data", passedStudents);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("statusCode", 404);
                response.put("message", "No passed students found.");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.put("statusCode", 500);
            response.put("message", "Failed to retrieve results.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
