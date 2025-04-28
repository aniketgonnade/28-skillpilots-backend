package com.skilladmin.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import com.skilladmin.model.*;
import com.skilladmin.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skilladmin.dto.CollegeInternalReqDto;
import com.skilladmin.service.CompAdvertismentService;
import com.skilladmin.service.InternshipService;
import com.skilladmin.service.StudentService;
import com.skilladmin.service.UserService;
import com.skilladmin.util.ProjectConstants;


@RestController
@CrossOrigin(origins = {"*"})

public class StudentRequestContrkller {

    private final CompAdvertismentService advertismentService;
    private final ObjectMapper objectMapper;

    private final StudentService studentService;

    public StudentRequestContrkller(CompAdvertismentService advertismentService, StudentService studentService, ObjectMapper objectMapper) {
        this.advertismentService = advertismentService;
        this.studentService = studentService;
        this.objectMapper = objectMapper;
    }

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StudentRequestRepository studentRequestRepository;
    @Autowired
    private CompanyInternalReqRepository companyInternalReqRepository;
    @Autowired
    private CollegeInternalReqRepository collegeInternalReqRepository;
    @Autowired
    private InternshipService internshipService;
    @Autowired
    private CollegeRepository collegeRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private UserService userService;

    @PostMapping("api/apply")
    public ResponseEntity<Object> applyInternship(@RequestParam("advId") Long id, @RequestParam("studentId") Long student_id
    ) {
        StudentRequest studentRequest = new StudentRequest();
        String body = "<h1 style=\"font-size:55px;margin:20px;text-align:center\">Welcome to <span style=\"color:#1974c3;\">Skill<span style=\"color:#01af36;\">Pilots</span></h1>"
                + "<p style=\"text-align:center\">New Student Applied Internship.!! Please check In Skillpilot - <strong>"
                + "<p style=\"text-align:center\">Please Follow the link<br>"
                + "Click <a href=\"https://www.skillpilots.com\"><strong>here</strong></a>"

                + ProjectConstants.mail_footer;
        Optional<CompAdvertisement> advertisemnet = advertismentService.getAdvertisemnet(id);
        if (!advertisemnet.isEmpty()) {
            CompAdvertisement compAdvertisement = advertisemnet.get();
            studentRequest.setAdvertisement_id(id);
            studentRequest.setCompany_id(compAdvertisement.getCompanyId());
            studentRequest.setAdvertisement_id(student_id);
            studentRequest.setDuration(compAdvertisement.getDuration());
            studentRequest.setTechnology_name(compAdvertisement.getTechnology());
            studentRequest.setCreation_date(new Date());
            studentRequest.setApproval_status("pending");
            studentRequest.setStudent_id(student_id);
            studentService.applyRequest(studentRequest);

            User user = userRepository.findById(student_id).orElseThrow(() -> new RuntimeException("Student Not Found"));
            if (user.getRole().equals("0")) {
                College college = collegeRepository.findById((long) user.getCollege_id()).get();

                List<User> collegeUser = userRepository.findByEmailIdCommonForNotification(college.getEmail_id(), user.getDepartment());
                for (User hodOrCordinator : collegeUser) {
                    userService.sendVerificationEmail(hodOrCordinator.getEmail(), "New Notification From SkilPilot", body);

                }
            }
            Company company = companyRepository.findById(compAdvertisement.getCompanyId()).orElseThrow(() -> new RuntimeException("Company Not Found"));
            userService.sendVerificationEmail(company.getEmail_id(), "New Notification From SkilPilot", body);
            User hr = userRepository.findByEmailAndRole2(company.getEmail_id()).get();
            userService.sendVerificationEmail(hr.getEmail(), "New Notification From SkilPilot", body);

        }
        return new ResponseEntity<Object>(studentRequest, HttpStatus.ACCEPTED);
    }


    @GetMapping("api/internshipReqStatus")
    public ResponseEntity<?> internships(@RequestParam Long studentId) {

        User user = userRepository.findById(studentId).get();
        if (user == null) {
            return ResponseEntity.status(401).body("Unauthorized: User not logged in");
        }

        List<Map<String, Object>> studentAppliedRequestList = new ArrayList<>();
        Map<Map<String, Object>, String> map = new HashMap<>();
        List<CollegeInternalReqDto> cirlist = new ArrayList<>();

        List<Object[]> studentAppliedRequest = studentRequestRepository.getAppliedStudentsOfCompany(user.getId());
        System.out.println("Student Request " + studentAppliedRequest);

        // Transform the list of arrays into a list of maps with meaningful keys
        for (Object[] request : studentAppliedRequest) {
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("id", request[0]);
            requestMap.put("studentId", request[1]);
            requestMap.put("collegeId", request[2]);
            requestMap.put("departmentId", request[3]);
            requestMap.put("technology", request[4]);
            requestMap.put("duration", request[5]);
            requestMap.put("companyId", request[6]);
            requestMap.put("jobId", request[7]);
            requestMap.put("appliedDate", request[8]);
            requestMap.put("status", request[9]);
            requestMap.put("jobTitle", request[10]);
            requestMap.put("companyName", request[11]);
            requestMap.put("rejectionMsg", request[12]);
            requestMap.put("testDataStatus", request[13]);
            requestMap.put("testDataId", request[14]);
            requestMap.put("testExpirationDate", request[15]);
            studentAppliedRequestList.add(requestMap);
        }

        Integer collegeId = user.getCollege_id();
        if (collegeId != null && collegeId != 0) {
            cirlist.addAll(studentService.getClgInternalReqOfStudentWithDept(
                    user.getId(), user.getId(), (long) user.getCollege_id(), new Date(), "A"));


            if (cirlist != null && !cirlist.isEmpty()) {
                for (CollegeInternalReqDto objData : cirlist) {
                    Map<String, Object> slist = new HashMap<>();
                    String approvalStatus = objData.getApprovalStatus();

                    slist.put("companyName", objData.getCompanyName());
                    slist.put("technology", objData.getTechnology());
                    slist.put("duration", objData.getDuration());
                    slist.put("requestId", objData.getRequestId());
                    slist.put("expirationDate", objData.getExpirationDate());
                    slist.put("rejectionMsg", objData.getRejectionMsg()); // Reject reason also used below as index 5, do not change
                    slist.put("testDataStatus", objData.getTestDataStatus()); // Student test status
                    slist.put("testDataId", objData.getTestDataId());

                    switch (approvalStatus) {
                        case ProjectConstants.coll_internal_req_approval_status_approved:
                            ExternalRequest externalRequest = studentService.getExternalRequestByERid((Long) objData.getAgainstExtReq());
                            System.out.println("External Request " + externalRequest);
                            if (null != externalRequest) {
                                String companyApprovalStatus = externalRequest.getApproval_status();
                                if (!companyApprovalStatus.equals(ProjectConstants.external_req_approval_status_test_assigned))
                                    slist.remove("testDataId"); // remove test id if test status has been changed

                                switch (companyApprovalStatus) {
                                    case ProjectConstants.external_req_approval_status_test_assigned:
                                        if (slist.get("testDataStatus").equals(ProjectConstants.student_test_data_status_disqualified) ||
                                                slist.get("testDataStatus").equals(ProjectConstants.student_test_data_status_failed) ||
                                                slist.get("testDataStatus").equals(ProjectConstants.student_test_data_status_passed))
                                            map.put(slist, "Test submitted. Please wait for the Industry to evaluate your result.");
                                        else if (slist.get("testDataStatus").equals(ProjectConstants.student_test_data_status_pending))
                                            map.put(slist, "Test Assigned.");
                                        break;
                                    case ProjectConstants.external_req_approval_status_pending:
                                        map.put(slist, "Pending Approval From Industry");
                                        break;
                                    case ProjectConstants.external_req_approval_status_approved:
                                        map.put(slist, "In Process within Industry");
                                        break;
                                    case ProjectConstants.external_req_approval_status_cancelled:
                                        map.put(slist, "Request Cancelled");
                                        break;
                                    case ProjectConstants.external_req_approval_status_dept_del:
                                        map.put(slist, "Request deleted");
                                        break;
                                    case ProjectConstants.external_req_approval_status_rejected:
                                        slist.put("rejectionMsg", externalRequest.getRejection_msg());
                                        map.put(slist, "Request Rejected by industry");
                                        break;
                                    case ProjectConstants.external_req_approval_status_assigned:
                                        CompanyInternalRequest compintreq = companyInternalReqRepository.getCompIntReqFromExtReqId((Long) objData.getAgainstExtReq());
                                        if (null != compintreq) {
                                            String compApprovalStatus = compintreq.getApproval_status();
                                            switch (compApprovalStatus) {
                                                case ProjectConstants.comp_internal_req_approval_status_internship_lead:
                                                case ProjectConstants.comp_internal_req_approval_status_manager:
                                                    map.put(slist, "In Process within Industry");
                                                    break;
                                                case ProjectConstants.comp_internal_req_approval_status_approved:
                                                    map.put(slist, "Internship has been scheduled");
                                                    break;
                                                case ProjectConstants.comp_internal_req_approval_status_cancelled:
                                                    map.put(slist, "Cancelled from within the Industry");
                                                    break;
                                            }
                                        }
                                        break;
                                }
                            }
                            break;
                        case ProjectConstants.coll_internal_req_approval_status_HOD:
                            map.put(slist, "Pending HOD Approval");
                            break;
                        case ProjectConstants.coll_internal_req_approval_status_III:
                            map.put(slist, "Pending T&P/III Head Approval");
                            break;
                        case ProjectConstants.coll_internal_req_approval_status_cancelled:
                            map.put(slist, "Request Cancelled");
                            break;
                        case ProjectConstants.coll_internal_req_approval_status_rejected:
                            map.put(slist, "Request Rejected");
                            break;
                        case ProjectConstants.coll_internal_req_approval_status_dept_del:
                            map.put(slist, "Department Deleted");
                            break;
                    }
                }
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", 200);

        response.put("studentsAppliedToCompanies", studentAppliedRequestList);
        response.put("map", map);
        System.out.println("student internal " + map);

        return ResponseEntity.ok(response);
    }

    @GetMapping("api/internships")
    public ResponseEntity<Object> getInternships(@RequestParam Long studentId, @RequestParam String status) {
        Map<String, Object> response = new HashMap<>();

        ArrayList<Internships> internships = internshipService
                .getPastOrPendingOrCancelledInternshipsOfStudent(studentId, status);


        response.put("statusCode", 200);
        response.put("pendingInternships", internships.isEmpty() ? new ArrayList<>() : internships);


        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/getInternshipStatus")
    public Map<String, Object> getInternshipStatus(@RequestParam Long studentId) {

        Map<String, Object> response = new HashMap<>();


        boolean internshipStatusOfStudent = internshipService.countByStudentIdAndStatusInNative(studentId);
        response.put("approvalStatus", internshipStatusOfStudent);

        if (internshipStatusOfStudent) {
            return response;
        }

        // Check if already applied to the same advertisement
        StudentRequest studentRequest = studentService.getStudentReqAgainstAdvNotPresent(studentId);
        if (studentRequest != null) {
            response.put("approvalStatus", studentRequest.getApproval_status());
        } else {
            response.put("approvalStatus", "none");
        }

        return response;
    }


}
	
	
	

