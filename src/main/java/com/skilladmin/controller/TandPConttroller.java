package com.skilladmin.controller;

import com.skilladmin.dto.PlacementRequest;
import com.skilladmin.dto.RecruitmentDTO;
import com.skilladmin.dto.StudentPerformanceDTO;
import com.skilladmin.dto.UpdateRoundRequest;
import com.skilladmin.model.*;
import com.skilladmin.repository.*;
import com.skilladmin.service.InternshipService;
import com.skilladmin.service.PlacementService;

import com.skilladmin.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = { "*" })
@RestController
@Slf4j
public class TandPConttroller {

	@Autowired
	private PlacementService placementService;
	@Autowired
	private StudentPastRepository studentPastRepository;
	@Autowired
	private StudentService studentService;
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private RecruitmentStatusRepo recruitmentStatusRepo;
	@Autowired
	private RecruitmentRepository recruitmentRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CollegeRepository collegeRepository;
	@Autowired
	private InternshipService internshipService;

	@PostMapping("/addPlacement")
	public ResponseEntity<Recruitment> createPlacement(
	        @RequestBody PlacementRequest placementRequest,
	        @RequestParam Long collegeId,
	        @RequestParam(value = "placementId", required = false) Long placementId) {
	    System.out.println("Placement Id is: " + placementId);
	    String departmentIds = placementRequest.getDepartmentIds();
	    if (placementId != null) {
	        // Fetch existing placement
	        Recruitment existingPlacement = recruitmentRepository.findById(placementId)
	                .orElseThrow(() -> new RuntimeException("Placement not found for this id :: " + placementId));
	        // Update Recruitment fields
	        existingPlacement.setCompanyName(placementRequest.getPlacement().getCompanyName());
	        existingPlacement.setContactNo(placementRequest.getPlacement().getContactNo());
	        existingPlacement.setCtc(placementRequest.getPlacement().getCtc());
	        existingPlacement.setEmail(placementRequest.getPlacement().getEmail());
	        existingPlacement.setProfile(placementRequest.getPlacement().getProfile());
	        existingPlacement.setDepartmentIds(departmentIds);
	        // Update PlacementDetails fields
	        RecruitmentDetails placementDetails = existingPlacement.getPlacementDetails();
	        if (placementDetails == null) {
	            placementDetails = new RecruitmentDetails();
	        }
	        placementDetails.setWebsite(placementRequest.getPlacementDetails().getWebsite());
	        placementDetails.setJobTitle(placementRequest.getPlacementDetails().getJobTitle());
	        placementDetails.setJobDescription(placementRequest.getPlacementDetails().getJobDescription());
	        placementDetails.setIsOpen(placementRequest.getPlacementDetails().getIsOpen());
	        placementDetails.setInterviewStartDate(placementRequest.getPlacementDetails().getInterviewStartDate());
	        placementDetails.setInterviewEndDate(placementRequest.getPlacementDetails().getInterviewEndDate());
	        existingPlacement.setPlacementDetails(placementDetails);
	        // Save updated placement
	        recruitmentRepository.save(existingPlacement);
	        return new ResponseEntity<>(existingPlacement, HttpStatus.OK);
	    } else {
	        // Create a new placement if placementId is null
	        Recruitment createdPlacement = placementService.createPlacement(
	                placementRequest.getPlacement(),
	                placementRequest.getPlacementDetails(),
	                departmentIds,
	                collegeId);
	        return new ResponseEntity<>(createdPlacement, HttpStatus.CREATED);
	    }
	}

	@GetMapping("/list")
	public ResponseEntity<Object> getPlacementList(@RequestParam Long collegeId,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "6") int size) {
		HashMap<Object, Object> response = new HashMap<>();
		Page<Recruitment> placements = placementService.getPlacementList(collegeId, page, size);
		long totalPlacements = placements.getTotalElements(); // Get the total count of placements

		if (!placements.isEmpty()) {
			response.put("msg", "Placement received Succesfully");
			response.put("placeme", totalPlacements);
			response.put("count", placements.getTotalElements());
			response.put("currentPage", placements.getNumber());
			response.put("totalPages", placements.getTotalPages());
			response.put("placements", placements.getContent());

		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("/byDepartment")
	public List<Recruitment> getPlacementsByDepartmentId(@RequestParam Long departmentId) {
		return placementService.getPlacementsByDepartmentId(departmentId);
	}

	@PutMapping("/update")
	public ResponseEntity<String> updatePlacement(@RequestParam Long id, @RequestBody RecruitmentDTO recruitmentDTO) {
		try {
			Recruitment updatedPlacement = placementService.editPlacement(id, recruitmentDTO);

			return ResponseEntity.ok("Placement updated successfully: " + updatedPlacement.getPlacementId());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Placement not found for ID: " + id);
		}
	}

	@GetMapping("/recruitment")
	public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getUser(@RequestParam Long collegeId,
			@RequestParam Long reqruitmentId, @RequestParam String status) {
		List<Map<String, Object>> result = placementService.findUserRecruitmentDetails(collegeId, reqruitmentId,
				status);
		log.info("Request List{}", result);

		String message = result.isEmpty() ? "No recruitment details found for the student"
				: "Recruitment details retrieved successfully";
		ApiResponse<List<Map<String, Object>>> response = new ApiResponse<>(message, result);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/companyRecruitmentHistory")
	public ResponseEntity<ApiResponse<List<Map<String, Object>>>> companyRecruitmentHistory(
			@RequestParam Long company_id, @RequestParam Long drive_id, @RequestParam String status) {
		List<Map<String, Object>> result = userRepository.findCompanyrecruitment(company_id, drive_id, status);
		log.info("Request List{}", result);

		String message = result.isEmpty() ? "No recruitment details found for the student"
				: "Recruitment details retrieved successfully";
		ApiResponse<List<Map<String, Object>>> response = new ApiResponse<>(message, result);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/recruitment-details")
	public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getUserRecruitmentDetails(
			@RequestParam Long studentId) {
		List<Map<String, Object>> result = placementService.findByStudentRecruitmentDetails(studentId);

		String message = result.isEmpty() ? "No recruitment details found for the student"
				: "Recruitment details retrieved successfully";
		ApiResponse<List<Map<String, Object>>> response = new ApiResponse<>(message, result);

		return ResponseEntity.ok(response);
	}

	@PutMapping("/deactivateRecruitment")
	public ResponseEntity<String> deactivateRecruitment(@RequestParam Long id) {
		try {
			Recruitment recruitment = placementService.deactivateRecruitment(id);
			String msg = recruitment.getStatus().equals("A") ? "Activated successfully" : "Deactivate successfully";
			return ResponseEntity.ok(msg);
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	@GetMapping("/performance")
	public ResponseEntity<List<StudentPerformanceDTO>> getStudentPerformance(@RequestParam String ssc,
			@RequestParam String hsc, @RequestParam String ug, @RequestParam Long collegeId,
			@RequestParam Long placementId) {
		TimeZone timeZone = TimeZone.getTimeZone("Asia/Kolkata");
		Calendar calendar = Calendar.getInstance(timeZone);

		int nextYear = calendar.get(Calendar.YEAR) + 1;
		System.out.println(nextYear);

		List<Object[]> results = studentPastRepository.findStudentPerformances(ssc, hsc, ug, collegeId, placementId,
				String.valueOf(nextYear));
		List<StudentPerformanceDTO> studentPerformances = new ArrayList<>();

		for (Object[] result : results) {
			StudentPerformanceDTO dto = new StudentPerformanceDTO();
			Long studentId = ((Number) result[0]).longValue();
			String studentName = (String) result[1];
			String sscDescription = (String) result[2];
			String hscDescription = (String) result[3];
			String ugDescription = (String) result[4];
			String status = (String) result[5];
			dto.setStudentName(studentName);
			dto.setHscDescription(hscDescription);
			dto.setSscDescription(sscDescription);
			dto.setUgDescription(ugDescription);
			dto.setStudentId(studentId);
			String s = status == null ? "NA" : status;
			dto.setStatus(s);
			System.out.println("Student Name" + studentName);
			studentPerformances.add(dto);
		}

		return ResponseEntity.status(200).body(studentPerformances);
	}

	@GetMapping("/filerByYear")
	public ResponseEntity<List<StudentPerformanceDTO>> filter(@RequestParam String ssc, @RequestParam String hsc,
			@RequestParam String ug, @RequestParam Long collegeId, @RequestParam Long placementId,
			@RequestParam List<String> years) {

		List<Object[]> results = studentPastRepository.findStudentsByCriteria(ssc, hsc, ug, collegeId, years);
		List<StudentPerformanceDTO> studentPerformances = new ArrayList<>();

		for (Object[] result : results) {
			StudentPerformanceDTO dto = new StudentPerformanceDTO();
			Long studentId = ((Number) result[0]).longValue();

			// Fetch recruitment status based on studentId and placementId
			RecruitmentStatus status = recruitmentStatusRepo.findByStudentIdAndReqruitmentId(studentId, placementId);

			String studentName = (String) result[1];
			String sscDescription = (String) result[2];
			String hscDescription = (String) result[3];
			String ugDescription = (String) result[4];

			dto.setStudentName(studentName);
			dto.setHscDescription(hscDescription);
			dto.setSscDescription(sscDescription);
			dto.setUgDescription(ugDescription);
			dto.setStudentId(studentId);

			// Set status to the retrieved status if found, otherwise "NA"
			dto.setStatus(status != null ? status.getStatus() : "NA");

			log.info("Student Name: {}, Status: {}", studentName, dto.getStatus());

			studentPerformances.add(dto);
		}

		return ResponseEntity.status(200).body(studentPerformances);
	}

	@PostMapping("/send")
	public ResponseEntity<Object> sendRecruitmentToStudents(@RequestParam List<Long> studentIds,
			@RequestParam Long recruitmentId, @RequestParam Long collegeId) {
		try {
			// Call the service method to handle recruitment assignment and notification
			Object response = studentService.sendRecruitmentToStudent(studentIds, recruitmentId, collegeId);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error sending recruitment: " + e.getMessage());
		}
	}

	@GetMapping("/findDept")
	public ResponseEntity<Object> getDept(@RequestParam String email) {
		List<Map<String, Object>> responseList = new ArrayList<>();
		College college = collegeRepository.findByEmail(email);
		List<Object[]> dept1 = departmentRepository.getDept(college.getCollege_id());

		for (Object[] o : dept1) {
			Map<String, Object> map = new HashMap<>(); // Create a new map for each entry
			map.put("id", o[0]);
			map.put("deptName", o[1]);
			responseList.add(map);
		}

		return ResponseEntity.ok(responseList);
	}

	@GetMapping("/dept")
	public ResponseEntity<Object> dept(@RequestParam Long collegeId) {
		List<Map<String, Object>> responseList = new ArrayList<>();
		// College college = collegeRepository.findByEmail(email);
		List<Object[]> dept1 = departmentRepository.getDept(collegeId);

		for (Object[] o : dept1) {
			Map<String, Object> map = new HashMap<>(); // Create a new map for each entry
			map.put("id", o[0]);
			map.put("deptName", o[1]);
			System.out.println("uiehiuediehfeiheidh" + o[0]);
			responseList.add(map);
		}

		return ResponseEntity.ok(responseList);
	}

	@GetMapping("/studentDriveRequest")
	public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getUCompanyDriveApplyStudents(
			@RequestParam Long companyId, @RequestParam String jobRole) {
		{
			List<Map<String, Object>> companyDriveApplyStudents = placementService
					.findCompanyDriveApplyStudents(companyId, jobRole);
			String message = companyDriveApplyStudents.isEmpty() ? "No recruitment details found for the student"
					: "Recruitment details retrieved successfully";
			ApiResponse<List<Map<String, Object>>> response = new ApiResponse<>(message, companyDriveApplyStudents);
			return ResponseEntity.ok(response);

		}
	}

	@PutMapping("/update-round")
	public ResponseEntity<Map<String, Object>> updateRound(@RequestBody UpdateRoundRequest request) {

		Map<String, Object> response = new HashMap<>();
		try {
			List<RecruitmentStatus> updatedStatuses = studentService.updateRound(request.getStudentIds(),
					request.getRecruitmentId(), request.getRound(), request.getRoundTime(), request.getRoundDate());

			response.put("message", "Round updated and emails sent successfully.");
			response.put("updatedStatuses", updatedStatuses);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("message", "Failed to update round or send emails.");
			response.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@GetMapping("/getRecruitmentsList")
	public ResponseEntity<List<Map<String, Object>>> getRecruitmentsByCollegeAndStudent(@RequestParam Long collegeId,
			@RequestParam Long studentId) {

		List<Object[]> recruitments = recruitmentRepository.findByCollegeAndStudent(collegeId, studentId);
		System.out.println("Recruitments: " + recruitments.toString());

		if (recruitments.isEmpty()) {
			return ResponseEntity.status(404).body(Collections.emptyList()); // Return 404 if no data found
		} else {
			List<Map<String, Object>> result = new ArrayList<>();

			for (Object[] recruitment : recruitments) {
				Map<String, Object> recruitmentData = new HashMap<>();

				recruitmentData.put("companyName", recruitment[0]);
				recruitmentData.put("profile", recruitment[1]);
				recruitmentData.put("placementId", recruitment[2]);
				recruitmentData.put("description", recruitment[3]);
				recruitmentData.put("city", recruitment[4]);
				recruitmentData.put("ctc", recruitment[5]);
				recruitmentData.put("status", recruitment[6]);
				recruitmentData.put("skills", recruitment[7]);
				result.add(recruitmentData);
			}
			return ResponseEntity.ok(result); // Return 200 OK with the direct data
		}
	}

	@GetMapping("/studentHistory")
	public ResponseEntity<List<Map<String, Object>>> getStudentHistory(@RequestParam Long collegeId,
			@RequestParam Long studentId) {
		List<Object[]> studentAppliedHistory = recruitmentRepository.getStudentAppliedHistory(collegeId, studentId);
		ArrayList<Map<String, Object>> applliedHistory = new ArrayList<>();

		if (studentAppliedHistory != null) {
			for (Object[] o : studentAppliedHistory) {
				Map<String, Object> history = new HashMap<>();
				history.put("companyName", o[0]);
				history.put("profile", o[1]);
				history.put("placementId", o[2]);
				history.put("status", o[3]);
				applliedHistory.add(history);

			}

		}
		return ResponseEntity.ok(applliedHistory);
	}

	@GetMapping("/status")
	public ResponseEntity<String> getRecruitmentStatus(@RequestParam Long collegeId, @RequestParam Long studentId) {

		List<Object[]> recruitments = recruitmentRepository.findByCollegeAndStudent(collegeId, studentId);

		// Check if recruitments list is empty
		if (recruitments.isEmpty()) {
			return ResponseEntity.ok("not found");
		} else {
			return ResponseEntity.ok("success");
		}
	}
	
	
	
	@GetMapping("/selectedStatusRound")
	public List<Map<String, Object>> listSelectedRound()
	{
		List<Map<String, Object>> selectedRoundsWithCompanyAndStudent = recruitmentStatusRepo.findSelectedRoundsWithCompanyAndStudent();
		return selectedRoundsWithCompanyAndStudent;
		
	}

	@GetMapping("/completeInternList")
    public List<Map<String, Object>> viewCompletedInternshipList() {
        List<Map<String, Object>> byOrderInternshipCompletedList = internshipService.findByOrderInternshipCompletedList();
        return byOrderInternshipCompletedList;
    }
	
}