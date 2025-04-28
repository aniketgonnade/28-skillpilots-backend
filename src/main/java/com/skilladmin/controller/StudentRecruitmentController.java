package com.skilladmin.controller;

import com.skilladmin.dto.RecruitmentWithStatusDTO;
import com.skilladmin.model.Company;
import com.skilladmin.model.CompanyDrive;
import com.skilladmin.model.RecruitmentStatus;
import com.skilladmin.model.User;
import com.skilladmin.repository.CompanyDriveRepository;
import com.skilladmin.repository.CompanyRepository;
import com.skilladmin.repository.RecruitmentStatusRepo;
import com.skilladmin.repository.StudentRepository;
import com.skilladmin.repository.UserRepository;
import com.skilladmin.service.EmailService;
import com.skilladmin.service.StudentService;
import com.skilladmin.util.ProjectConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = {"*"})
public class StudentRecruitmentController {
    @Autowired
    private EmailService emailService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private RecruitmentStatusRepo statusRepo;
    @Autowired
    private CompanyDriveRepository companyDriveRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/apply")
    public ResponseEntity<Object> applyDrive(@RequestParam Long studentId,
                                             @RequestParam Long recruitId,
                                             @RequestParam(value = "collegeId", required = false) Long collegeId) {
        try {
            RecruitmentStatus status = studentService.applyDrive(studentId, recruitId, collegeId);

            return ResponseEntity.ok(Map.of("msg","Application submitted successfully for recruitment ID: " + recruitId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Student ID or Recruitment ID not found." + e.getMessage());
        }
    }

    @GetMapping("/applicant-count")
    public ResponseEntity<Object> getApplicantCount() {
        List<RecruitmentStatus> applicants = studentService.findAppicanCount();

        if (applicants.isEmpty()) {
            return ResponseEntity.accepted().body(Map.of("count", 0)); // Return 0 if no applicants are found
        } else {
            long count = applicants.size(); // Get the count of applicants
            return ResponseEntity.accepted().body(Map.of("count", count)); // Return 0 if no applicants are found
        }
    }


    @PutMapping("/status")
    public ResponseEntity<Map<String, Object>> changeRecruitmentStatus(
            @RequestParam Long statusId,
            @RequestParam String status) {

        Map<String, Object> response = new HashMap<>();

        try {
            RecruitmentStatus updatedStatus = studentService.changeStatus(statusId, status);

            response.put("message", "Recruitment status updated successfully");
            response.put("status", updatedStatus);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            response.put("message", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/applyCompDrive")
    public ResponseEntity<Object> applyCompanyDrive(@RequestParam Long companyId, @RequestParam Long driveId, @RequestParam Long studentId) {
        // Create a new recruitment status
        RecruitmentStatus status = new RecruitmentStatus();
        status.setCompanyId(companyId);
        status.setStatus("Applied");
        status.setDriveId(driveId);
        status.setStudentId(studentId);
        statusRepo.save(status);

        // Fetch the student (user)
        User user = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student Not found"));

        Optional<Company> company = companyRepository.findById(companyId);
        if (company.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found.");
        }

        CompanyDrive drive = companyDriveRepository.findById(driveId)
                .orElseThrow(() -> new RuntimeException("Drive not found."));

        // Prepare email body
        String mailBody = "<h1 style=\"font-size:55px;margin:20px;text-align:center\">"
                + "<span style=\"color:#1974c3;\">Skill<span style=\"color:#01af36;\">Pilots</span></h1>"
                + "<p style=\"text-align:center\"><strong style=\"color:#414ea4\">" + company.get().getCompany_name() + "</strong>,</p>"
                + "<p style=\"text-align:center\">We are pleased to inform you that a new candidate has applied for the position of <strong>" + drive.getJobRole() + "</strong> as part of your ongoing recruitment drive.</p>"
                + "<ul style=\"text-align:center; list-style-type:none; padding:0;\">"
                + "<li><strong>Candidate Name:</strong> " + user.getUsername() + "</li>"
                + "<li><strong>Email:</strong> " + user.getEmail() + "</li>"
                + "<li><strong>Application Date:</strong> " + status.getApplyDate() + "</li>"
                + "</ul>"
                + "<p style=\"text-align:center\">Please log in to your company portal at <strong style=\"color:#414ea4\"><a href=\"http://www.skillpilots.com\" style=\"color:#414ea4; text-decoration:none;\">SkillPilots</a></strong> to review the candidate’s application and take the necessary steps.</p>"
                + "<p style=\"text-align:center\">Feel free to reach out to us if you need any assistance.<br>"
                + "Best regards,<br>The SkillPilots Team</p>"
                + ProjectConstants.mail_footer;

        // Send email to the company
        emailService.sendVerificationEmail(company.get().getEmail_id(), "New Candidate Application", mailBody);
        // Prepare email body for the candidate
        String candidateMailBody = "<h1 style=\"font-size:55px;margin:20px;text-align:center\">"
                + "<span style=\"color:#1974c3;\">Skill<span style=\"color:#01af36;\">Pilots</span></h1>"
                + "<p style=\"text-align:center\"><strong style=\"color:#414ea4\"> Dear," + user.getName() + "</strong>,</p>"
                + "<p style=\"text-align:center\">Thank you for applying to the position of <strong>" + drive.getJobRole() + "</strong> at <strong>" + company.get().getCompany_name() + "</strong>.</p>"
                + "<p style=\"text-align:center\">We have received your application, and the company will review your details shortly. You will be updated on further steps via email.</p>"
                + "<p style=\"text-align:center\">Best of luck with your application process!</p>"
                + "<p style=\"text-align:center\">Feel free to reach out to us if you need any assistance.<br>"
                + "Best regards,<br>The SkillPilots Team</p>"
                + ProjectConstants.mail_footer;

        // Send email to the candidate
        emailService.sendVerificationEmail(user.getEmail(), "Application Received", candidateMailBody);


        // Return success response
        return ResponseEntity.ok(Map.of("msg","Application submitted successfully for drive ID: " + driveId));
    }

    @GetMapping("/available-with-status")
    public ResponseEntity<List<RecruitmentWithStatusDTO>> getRecruitmentsWithStatus(
            @RequestParam Long collegeId,
            @RequestParam Long studentId) {
        List<RecruitmentWithStatusDTO> recruitments = studentService.getRecruitmentsWithStatus(collegeId, studentId);
        return new ResponseEntity<>(recruitments, HttpStatus.OK);
    }

    @GetMapping("/appliedStatusStudent")
    public ResponseEntity<Object> getAppliedStatusForStudent(@RequestParam Long studentId) {
        try {
            List<Map<String, Object>> appliedStatusForStudent = statusRepo.getAppliedStatusForStudent(studentId);

            // Remove null keys from each map in the list
            if (appliedStatusForStudent != null) {
                for (Map<String, Object> row : appliedStatusForStudent) {
                    row.entrySet().removeIf(entry -> entry.getKey() == null);
                }
            }

            // Return the applied status with OK status
            if (appliedStatusForStudent == null || appliedStatusForStudent.isEmpty()) {
                return new ResponseEntity<>("No applied status found for the student.", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(appliedStatusForStudent, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while fetching applied status.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/skills/{id}")
    public ResponseEntity<Map<String, String>> getStudentSkills(@PathVariable Long id) {
        return studentRepository.findById(id)
                .map(student -> ResponseEntity.ok(Map.of("skills", student.getSkills())))
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkIfExists(
            @RequestParam Long studentId,
            @RequestParam Long reqruitmentId) {
        boolean exists = statusRepo.existsByStudentIdAndReqruitmentId(studentId, reqruitmentId);
        return ResponseEntity.ok(exists);
    }

}
