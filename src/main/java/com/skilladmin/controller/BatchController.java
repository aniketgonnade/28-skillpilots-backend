package com.skilladmin.controller;

import com.skilladmin.dto.BatchAndFees;
import com.skilladmin.model.Batch;
import com.skilladmin.model.TrainingUsers;
import com.skilladmin.model.User;
import com.skilladmin.service.BatchService;
import com.skilladmin.service.TrainingUserService;
import com.skilladmin.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@CrossOrigin(origins = {"*"})
@RestController
@RequiredArgsConstructor
public class BatchController {

    private  final BatchService batchService;

    @Autowired
    private TrainingUserService trainingUserService;
    
    @PostMapping("/batch")
    public ResponseEntity<Object> createBatch(@RequestBody Batch batch){
       Batch batch1= batchService.createBatch( batch);
        return  ResponseEntity.ok().body(Map.of("msg","batch saves succesfully","batch",batch1,"status","200"));
    }

    @PostMapping("/batch-to-student")
    public ResponseEntity<HashMap<Object, Object>> assignBatchToStudent(
            @RequestParam Long studentId, @RequestParam Long batchId, @RequestParam Long batchfees) {
        
        HashMap<Object, Object> response = new HashMap<>();
        
        try {
            // Call service to assign batch
            response = batchService.assignBatchToStudents(studentId, batchId, batchfees);

            // Check the response message and return accordingly
            if ("Batch & fees assigned successfully.".equals(response.get("msg"))) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            response.put("msg", "An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @GetMapping("/getBatchAndFees")
    public  ResponseEntity<Optional<BatchAndFees>> getbatchAndFees(@RequestParam Long id){
    	
    	
    	
        return ResponseEntity.ok(trainingUserService.getbatchAndFees(id));
    }

    @GetMapping("/getbatch")
    public  ResponseEntity<Object> getBatch(@RequestParam Long batchId){
        return ResponseEntity.ok(batchService.getTutorByBatchId( batchId));
    }

    @GetMapping("/allBatches")
    public  ResponseEntity<Object> getAllBatches(){
    List<Batch> batch=   batchService.getAllBatch();
        if(!batch.isEmpty()){
            return  ResponseEntity.status(HttpStatus.OK).body(Map.of("msg","batch get Succesfully" ,
                    "students",batch,"status","200"));
        }
        else{
            return  ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(Map.of("msg","Not found","status","404"));
        }
    }

    @GetMapping("/getDataByBatchId")
    public ResponseEntity<Object> getDataByBatchId(@RequestParam Long batchId) {
        try {
            Batch batch = batchService.getDataByBatchId(batchId);
            return ResponseEntity.ok(batch);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }
    }


