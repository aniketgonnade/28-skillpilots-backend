package com.skilladmin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skilladmin.model.Batch;
import com.skilladmin.model.TrainingCourses;
import com.skilladmin.model.TrainingUsers;
import com.skilladmin.model.Tutors;
import com.skilladmin.repository.BatchRepository;
import com.skilladmin.repository.TrainingUserRepository;
import com.skilladmin.service.TrainingUserService;
import com.skilladmin.service.TutorsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"*"})
public class TutorsController {

    private final TutorsService tutorsService;

    private final BatchRepository batchRepository;

    private final TrainingUserRepository trainingUserRepository;

    @PostMapping("/createTutor")
    public ResponseEntity<Object> create(@RequestParam(value = "batchIds", required = false) List<Long> batchIds, @RequestBody Tutors tutors,@RequestParam(value = "address" ,required = false) String address) {
        return ResponseEntity.ok().body(tutorsService.create(batchIds, tutors, address));
    }


    @GetMapping("/api/getTutor")
    public ResponseEntity<Object> getTutor(@RequestParam Long tutorId) {
        return ResponseEntity.ok().body(tutorsService.getTutorById(tutorId));

    }

    @GetMapping("/getAllTutor")
    public ResponseEntity<Object> getAllTutors() {
        HashMap<String, Object> response = new HashMap<>();

        List<Tutors> tutors = tutorsService.getAllTutors();
        if (!tutors.isEmpty()) {
            response.put("msg", "Tutors Get Succesfully;");
            response.put("tutors", tutors);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } else {
            response.put("msg", "Tutors Not Get ;");
            return new ResponseEntity<>(response, HttpStatus.OK);

        }
    }

    @PostMapping(value = "/course", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> saveCourse(@RequestPart("coursesEntity") TrainingCourses coursesEntity, @RequestParam("file") MultipartFile file) throws IOException {
        HashMap<String, Object> response = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        //  TrainingCourses entityToPass = objectMapper.readValue(coursesEntity, TrainingCourses.class);
        try {
            TrainingCourses entity = tutorsService.saveCourse(coursesEntity, file);
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

    @GetMapping("/getCourses")
    public ResponseEntity<Object> getTrainingCources() {
        HashMap<String, Object> response = new HashMap<>();

        List<TrainingCourses> trainingCourses = tutorsService.getAll();

        if (!trainingCourses.isEmpty()) {
            response.put("msg", "Available cources");
            response.put("courses", trainingCourses);
            response.put("code", HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } else {
            response.put("msg", "Unable To Proceed");
            response.put("code", HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @PostMapping("/tutorImage")
    public ResponseEntity<Object> uploadImageManager(@RequestParam Long id,
                                                     @RequestParam("file") MultipartFile file) {
        try {
            Tutors trainingUsers = tutorsService.uploadTutorImage(id, file);
            return ResponseEntity.status(200).body(Map.of("msg", "Image uploaded successfully",
                    "imagePath", trainingUsers.getImagePath()));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("msg", "Image upload failed",
                    "error", e.getMessage()));
        }
    }

    @PutMapping("/deactivateTutor")
    public ResponseEntity<Object> deactivateUser(@RequestParam Long tutorId) {
        Tutors tutors = tutorsService.deactivateUser(tutorId);
        String message = tutors.isStatus() ? "Tutor Activated Successfully" : "Tutor Deactivated Successfully";

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("msg", message, "user", tutors));
    }


    @PostMapping("/changeTutor")
    public ResponseEntity<Object> changeTutor(@RequestParam Long tutorId, @RequestParam Long batchId) {

        // Fetch batch by batchId
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch Not Found with ID " + batchId));

        // Check if the batch already has the requested tutor assigned
        if (batch.getTutors().getTutorId().equals(tutorId)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("msg", "This tutor is already assigned to the same batch"));
        }

        // Fetch the new tutor by ID
        Tutors newTutor = tutorsService.getTutorById(tutorId);

        // Update the batch's tutor
        batch.setTutors(newTutor);
        batchRepository.save(batch);

        // Update the TrainingUsers object (if necessary)
        Tutors previousTutor = batch.getTutors();
        TrainingUsers trainingUsers = trainingUserRepository.findByTutorId(previousTutor.getTutorId());
        if (trainingUsers != null) {
            trainingUsers.setBatchId(batchId);
            trainingUserRepository.save(trainingUsers);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("msg", "Tutor changed successfully", "batch", batch));
    }


    @Operation(summary = "Edit Tutor api")
    @PutMapping("/editTutor")
    public ResponseEntity<Object> editTutor(@RequestParam Long tutorId, @RequestBody TrainingUsers tutors) {
        try {
            tutorsService.editTutor(tutorId, tutors);
            return ResponseEntity.ok().body("Tutors edited Successfully");
        } catch (Exception e) {
            return ResponseEntity.ok().body(e.getMessage());
        }
    }
}



