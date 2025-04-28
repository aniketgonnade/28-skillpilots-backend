package com.skilladmin.controller;

import com.skilladmin.model.TraineeFeedback;
import com.skilladmin.service.TraineeFeedBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = {"*"})
@RestController
public class TraineeFeedbackController {

    @Autowired
    private TraineeFeedBackService traineeFeedBackService;

    @PostMapping("/giveFeedback")
    private ResponseEntity<Object> giveFeedback(
            @RequestParam Long studentId, @RequestBody TraineeFeedback traineeFeedback) {

        try {
            TraineeFeedback traineeFeedback1 = traineeFeedBackService.giveFeedBack(studentId, traineeFeedback);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("msg", "Feedback Add succesfully", "feedback", traineeFeedback1));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("msg", e.getMessage()));
        }
    }

    @GetMapping("/feedback")
    private ResponseEntity<Object> getFeedbackOfStudent(@RequestParam Long studentId) {
        List<TraineeFeedback> studentFeedback = traineeFeedBackService.getStudentFeedback(studentId);
        if (!studentFeedback.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("msg", " Feedback found", "Feedback", studentFeedback));

        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Map.of("msg", "Not found"));

        }
    }

    @GetMapping("/weekList")
    private ResponseEntity<Object> getWeekList() {
        List<String> weekList = Arrays.asList("Week 1","Week 2","Week 3","Week 4","Week 5","Week 6","Week 7");
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("msg", "Weeks found", "weeks", weekList));
    }
}
