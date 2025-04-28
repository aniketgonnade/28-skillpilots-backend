package com.skilladmin.controller;

import com.skilladmin.model.PlanDetails;
import com.skilladmin.service.PlanDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(value = "*")
@RestController
public class PlanDetailsController {
    @Autowired
    private PlanDetailsService planDetailsService;


    // CREATE INTERNSHIP PLAN 
    @PostMapping("/createPlan")
    public ResponseEntity<?> cratePlan(@RequestBody PlanDetails planDetails) {
        HashMap<Object, Object> response = new HashMap<>();
        try {
            response.put("msg", "Plan Added Successfully");
            response.put("plan", planDetailsService.createPlan(planDetails));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("msg", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET PLAN
    @GetMapping("/plans")
    public ResponseEntity<?> getAllPlan() {
        HashMap<String, Object> response = new HashMap<>();
        List<PlanDetails> allPlans = planDetailsService.getAllPlans();

        if (allPlans.isEmpty()) {
            response.put("msg", "No plans available");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.put("msg", "Plans retrieved successfully");
        response.put("plans", allPlans);
        return ResponseEntity.ok(response);
    }
    
   
    // UPDATE PLAN
    @PutMapping("/updatePlan")
    public ResponseEntity<Map<String, Object>> updatePlan(@RequestBody PlanDetails planDetails) {
        Map<String, Object> response = new HashMap<>();
        try {
            PlanDetails updatedPlan = planDetailsService.updatePlan(planDetails);
            response.put("message", "Plan updated successfully!");
            response.put("updatedPlan", updatedPlan);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("message", "An error occurred while updating the plan.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
}