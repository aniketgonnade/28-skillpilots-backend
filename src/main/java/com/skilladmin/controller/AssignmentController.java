package com.skilladmin.controller;

import ch.qos.logback.core.boolex.EvaluationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skilladmin.model.*;
import com.skilladmin.repository.StudyMaterialRepository;
import com.skilladmin.repository.TraineAssignmentReposi;
import com.skilladmin.service.AssigmentService;
import com.skilladmin.service.StudyMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@CrossOrigin(origins = {"*"})
@RestController
public class AssignmentController {

    @Autowired
    private AssigmentService assigmentService;
    @Autowired
    private TraineAssignmentReposi traineAssignmentReposi;
    @Autowired
    private StudyMaterialService studyMaterialService;
    @Autowired
    private StudyMaterialRepository studentMaterial;


    @PostMapping("/addAssignment")
    public ResponseEntity<Object> addAssignment(
            @RequestParam("traineeAssignment") String traineeAssignment,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        HashMap<String, Object> response = new HashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TraineeAssignment assignment = objectMapper.readValue(traineeAssignment, TraineeAssignment.class);
            // System.out.println("sdhkh"+file);

            // Save the trainee assignment details to the database
            if (file.isEmpty() || file == null || Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
                TraineeAssignment data = traineAssignmentReposi.save(assignment);
                response.put("code", HttpStatus.OK);
                response.put("data", data);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                TraineeAssignment data = assigmentService.addAssignment(assignment, file);
                response.put("code", HttpStatus.OK);
                response.put("data", data);
                return new ResponseEntity<>(response, HttpStatus.OK);

            }
//            return ResponseEntity.ok("Assignment added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add assignment");
        }
    }


    @PostMapping("/assignAssignment")
    public ResponseEntity<Object> assignAssignMent(@RequestParam Long batchId, @RequestParam Long assignMentId) {

        try {
            Batch traineeAssignment = assigmentService.assignAssignMentToBach(batchId, assignMentId);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("msg", "Assignment assign Succesfully", "status", "200"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("msg", e.getMessage(), "status", "400"));
        }
    }


    @GetMapping("/getAssignment")
    public ResponseEntity<Object> getAssignMentByBatchId(@RequestParam Long batchId) {
        List<TraineeAssignment> traineeAssignment = assigmentService.getAssignmentByBatchId(batchId);
        if (!traineeAssignment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("msg", "Assignment got Succesfully", "status", "200", "asssignMent", traineeAssignment));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("msg", "Not found", "status", "400"));

        }
    }

    @PostMapping(value = "/studyMaterial", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> saveCourse(@RequestParam("studentMaterial") String studentMaterial, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        HashMap<String, Object> response = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        //  TrainingCourses entityToPass = objectMapper.readValue(coursesEntity, TrainingCourses.class);
        try {
            TraineeStudentMaterial material = objectMapper.readValue(studentMaterial, TraineeStudentMaterial.class);
            TraineeStudentMaterial entity = studyMaterialService.createStudyMatrial(material, file);
            response.put("msg", "Added Successfully");
            response.put("code", HttpStatus.OK);
            response.put("data", entity);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("msg", "Unable To Proceed");
            response.put("code", HttpStatus.BAD_REQUEST);
            response.put("data", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/getStudyMaterial")
    public ResponseEntity<Object> getStudytByBatchId(@RequestParam Long batchId) {
        List<TraineeStudentMaterial> traineeAssignment = studentMaterial.getStudyMaterialBybatch(batchId);
        if (!traineeAssignment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("msg", "StudyMaterial", "status", "200", "asssignMent", traineeAssignment));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("msg", "Not found", "status", "400"));

        }
    }

    @PostMapping("/solvedAssignment")
    public ResponseEntity<Object> uploadSolvedAssigment(@RequestParam Long studentId, @RequestParam Long batchId, @RequestParam Long assignMentId,
                                                        @RequestParam("file") MultipartFile file,@RequestParam(value = "description",required = false)String description) {
        HashMap<String, Object> response = new HashMap<>();

        try {
            StudentAssignment studentAssignment = assigmentService.uploadSolvedAssinment(studentId, batchId, assignMentId, file,description);
            response.put("msg", "Added Successfully");
            response.put("code", HttpStatus.OK);
            response.put("data", studentAssignment);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("msg", "Unable To Proceed");
            response.put("code", HttpStatus.BAD_REQUEST);
            response.put("data", e.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }

    @GetMapping("/getSolvedAssignments")
    public ResponseEntity<List<Map<String, Object>>> getStudentAssignmentWithTrainingUser(@RequestParam Long batchId) {
        Map<String, String> errorResponse = new HashMap<>();

        List<Map<String, Object>> response = assigmentService.getStudentAssignmentWithTrainingUser(batchId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/solvedAssignments")
    public ResponseEntity<Object> getAssignmentForStudent(@RequestParam Long studentId) {
        List<StudentAssignment> studentAssignments = assigmentService.getStudentAssignmentForStudent(studentId);
        if (!studentAssignments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of(
                    "msg", "assignment find", "assignment", studentAssignments));
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of(
                    "msg", "assignment not find"));
        }

    }
    @DeleteMapping("/assignmentDelete")
    public ResponseEntity<Object> deleteAssignmentById(@RequestParam Long id) {
        try {
            assigmentService.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("msg", "Assignment deleted successfully!"));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("msg", "Assignment not found with ID: " + id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("msg", "Error occurred while deleting the assignment"));
        }
    }

    @DeleteMapping("deleteStudyMaterial")
    public ResponseEntity<Object> deleteStudyMaterialyId(@RequestParam Long id){
        try {
            studentMaterial.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("msg", "Study Material deleted successfully!"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("msg", "Study Material not deleted !"));

        }
    }

    @DeleteMapping("/deleteSolve")
    public ResponseEntity<Object> delteSolveAssignment(@RequestParam Long id){
        try {
            assigmentService.deleteSolveAssignment(id);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("msg", "Assignment deleted successfully!"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("msg", e.getMessage()));

        }
}}
