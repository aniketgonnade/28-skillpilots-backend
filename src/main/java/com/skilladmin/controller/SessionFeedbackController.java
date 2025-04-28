package com.skilladmin.controller;

import com.skilladmin.model.SessionFeedback;
import com.skilladmin.service.SessionFeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = {"*"})
@RestController
public class SessionFeedbackController {

    private final SessionFeedbackService feedbackService;

    public SessionFeedbackController(SessionFeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/feedbackSave")
    public ResponseEntity<SessionFeedback> saveFeedback(@RequestBody SessionFeedback feedback) {
        return ResponseEntity.ok(feedbackService.saveFeedback(feedback));
    }

    @GetMapping("/feedbackAll")
    public ResponseEntity<List<SessionFeedback>> getAllFeedback() {
        return ResponseEntity.ok(feedbackService.getAllFeedBack());
    }

}
