package com.skilladmin.controller;

import com.skilladmin.dto.AppliedCompanyDTO;
import com.skilladmin.dto.UpdateRoundRequest;
import com.skilladmin.model.ApiResponse;
import com.skilladmin.model.CompanyDrive;
import com.skilladmin.model.Recruitment;
import com.skilladmin.model.RecruitmentStatus;
import com.skilladmin.repository.CompanyDriveRepository;
import com.skilladmin.repository.RecruitmentStatusRepo;
import com.skilladmin.service.PlacementService;
import com.skilladmin.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"*"})
@RestController
public class CompanyDriveController {
    @Autowired
    private PlacementService placementService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private CompanyDriveRepository companyDriveRepository;
    @Autowired
    private RecruitmentStatusRepo recruitmentStatusRepo;

    @GetMapping("/driveFilter")
    public ResponseEntity<Object> filterDrives(@RequestParam(required = false) String search
                                             ) {


        List<CompanyDrive> filteredDrives = placementService.getFilteredDrives(search);
        String message = filteredDrives.isEmpty() ? "No drives found for the specified filters." : "Drives found for the specified filters.";
        ApiResponse<List<CompanyDrive>> response = new ApiResponse<>(message, filteredDrives);
        return ResponseEntity.status(200).body(response);
    }
    @PutMapping("/update-company-drive")
    public ResponseEntity<Map<String, Object>> updateRound(
            @RequestParam List<Long> studentIds,
            @RequestParam Long driveId,
            @RequestParam String round,
            @RequestParam String roundTime,
            @RequestParam String roundDate,
            @RequestParam(value = "id",required = false) List<Long> id,
            @RequestParam(value = "meetLink", required = false) String meetLink) {

        Map<String, Object> response = new HashMap<>();
        try {
            List<RecruitmentStatus> updatedStatuses = studentService.updateCompanyRound(
                    studentIds,
                    driveId,
                    round,
                    roundTime,
                    roundDate,
                    id,
                    meetLink
            );

            System.out.println("ddddddddddddddd"+studentIds);
            response.put("message", "Round updated and emails sent successfully.");
            response.put("updatedStatuses", updatedStatuses);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Failed to update round or send emails.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @GetMapping("/driveById")
    public ResponseEntity<Object> getDriveById(@RequestParam Long driveId){
        CompanyDrive companyDrive = placementService.getCompanyDriveById(driveId);
        if (companyDrive!=null){
            return ResponseEntity.status(200).body(companyDrive);
        }
        else {
            return ResponseEntity.status(400).body("Company Drive Not found");
        }
    }

    @RequestMapping("/getRecruitmentById")
    public ResponseEntity<Object> getRecruitmentById(@RequestParam Long id) {
        Optional<Recruitment> recruitment = placementService.getRecruitmentId(id);

        return recruitment.<ResponseEntity<Object>>map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).body("Recruitment not found for ID: " + id));
    }

    
    @GetMapping("/getCompanyDrive")
    public ResponseEntity<?> getActiveCompanyDrivesStatus()
    {
        List<CompanyDrive> companyStatus = companyDriveRepository.findByStatus("A");
        if (!companyStatus.isEmpty())
        {
            return ResponseEntity.ok(companyStatus);
        }
        else
        {
            return ResponseEntity.status(404).body("Company Status is not Found");
        }
    }
    
    
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllRecruitments() {
        List<Recruitment> recruitments = placementService.getAllRecruitmentList();
        
        if (recruitments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(Collections.singletonMap("message", "No recruitment drives available"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Recruitment drives fetched successfully");
        response.put("data", recruitments);

        return ResponseEntity.ok(response);
    }
    
    
    @GetMapping("/stud_comp_req/{studentId}")
    public ResponseEntity<Object> getCompanyAppliedStudents(@PathVariable Long studentId){
    	
    	List<AppliedCompanyDTO> studentRequestCompany = recruitmentStatusRepo.getStudentRequestCompany(studentId);
        return ResponseEntity.ok(studentRequestCompany);

    }
    
}
    
    

