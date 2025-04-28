package com.skilladmin.controller;

import java.util.*;

import com.skilladmin.dto.AdminDto;
import com.skilladmin.dto.StudentListDto;
import com.skilladmin.model.*;
import com.skilladmin.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skilladmin.service.CollegeService;
import com.skilladmin.service.CompanyService;
import com.skilladmin.service.FeedbacklService;
import com.skilladmin.service.ProgresDataService;
import com.skilladmin.service.StudentService;
import com.skilladmin.service.UserService;

@RestController
@CrossOrigin(origins = {"*"})
public class MainController {
	@Autowired
	private CollegeService collegeService;
	@Autowired
	private UserService userService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private FeedbacklService feedbacklService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProgresDataService progresDataService;
	@GetMapping("/getAllCollege")
	public ResponseEntity<Object> getAllColleges() {

		HashMap<String, Object> response = new HashMap<>();
		List<Object[]> allColleges = collegeService.getAllColleges();
		if (allColleges != null && !allColleges.isEmpty()) {
			response.put("msg", "allColleges");
			response.put("allColleges", allColleges);

		} else {
			response.put("msg", "Not found");

		}
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@GetMapping("/getAllComp")
	public ResponseEntity<Object> getAllCompanies() {
	    HashMap<String, Object> response = new HashMap<>();
	    List<Object[]> allCompanies = collegeService.getAllCompanies();
	    if (allCompanies != null && !allCompanies.isEmpty()) {
	        response.put("msg", "allCompanies");
	        response.put("companies", allCompanies);
	    } else {
	        response.put("msg", "Not found");
	    }
	    
	    return new ResponseEntity<>(response, HttpStatus.OK);
	}



	@Operation(
			summary = "Retrieve all students",
			description = "Fetches a list of all students, with optional pagination and filtering by internal/external status."
	)
	@ApiResponses(value = {
			@ApiResponse(
					responseCode = "200",
					description = "Successfully retrieved the list of students",
					content = @Content(
							mediaType = "application/json"
							)

			) })
	@GetMapping("/getAllStudent")
	public ResponseEntity<Map<String, Object>> getPaginatedStudents(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<StudentListDto> studentPage = studentService.getAllStudentsList(page, size);
		Map<String, Object> response = new HashMap<>();
		response.put("students", studentPage.getContent());
		response.put("currentPage", studentPage.getNumber());
		response.put("totalItems", studentPage.getTotalElements());
		response.put("totalPages", studentPage.getTotalPages());

		return ResponseEntity.ok(response);
	}

	@GetMapping("/listStudents")
	public ResponseEntity<List<StudentListDto>> getAllStudents() {
		List<StudentListDto> allStudents = studentService.getStudentList();
		System.out.println("Fetched students: " + allStudents); // Debugging log
		return new ResponseEntity<>(allStudents, HttpStatus.OK); // Returning the list of DTOs
	}

	@GetMapping("/searchStudent")
	public ResponseEntity<Object> searchStudents(@RequestParam("name") String name,@RequestParam(defaultValue = "0") int page,
														@RequestParam(defaultValue = "10") int size) {
		Page<StudentListDto> students = studentService.searchStudentsByName(name,page,size);
		Map<String, Object> response = new HashMap<>();
		response.put("students", students.getContent());
		response.put("currentPage", students.getNumber());
		response.put("totalItems", students.getTotalElements());
		response.put("totalPages", students.getTotalPages());

		return ResponseEntity.ok(response);	}

		
	
	@PutMapping("/updateCollege")
	public ResponseEntity<Object> updateCollege(@RequestParam("college_id") Long college_id) {
		
		HashMap<String, Object> response = new HashMap<>();

		User updateCollege = collegeService.updateCollege(college_id);
		if(updateCollege!=null) {
			response.put("msg", "Verified Succesfully");
			response.put("Update", updateCollege);
			response.put("status", updateCollege.getVerified());

		}
		else {
			response.put("Not found", "Colleege Not found");
			
		}
		return new ResponseEntity<Object>(response, HttpStatus.OK);

		
	}

	@PutMapping("/updateCompany")
	public ResponseEntity<Object> updateCompany(@RequestParam("company_id") Long company_id ){
		HashMap<String, Object> response = new HashMap<>();
		User updateCompany = collegeService.updateCompany(company_id);
		if(updateCompany!=null) {
			response.put("msg", "Verified Succesfully");
			response.put("Update", updateCompany);
			response.put("status", updateCompany.getVerified());
		}
		else {
			response.put("Not found", "Company Not found");
			
		}
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	
	
	}
 

	@GetMapping("/getStudents")
	public ResponseEntity<Object> getStudentsWithCollege(@RequestParam String role, @RequestParam Long college_id){
		HashMap<String, Object> response = new HashMap<>();
		List<Object[]> studentsWithCollege = collegeService.getStudentsWithCollege(role, college_id);
		if(!studentsWithCollege.isEmpty()) {
			response.put("msg", "Students get Succesfully");
			response.put("students", studentsWithCollege);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
		}
		else {
			response.put("msg", "Not found");
			return new ResponseEntity<Object>(response, HttpStatus.NOT_FOUND);
		}
	}

	@Operation(
			summary = "Filter By Department,year,college with and without pagination",
			description = "Fetches a list of all students, with optional pagination and filtering by dept/year."
	)
	@GetMapping("/filterByDept")
	public ResponseEntity<Object> getStudentsByCollegeAndDept(
			@RequestParam("collegeId") Long collegeId,
			@RequestParam(value = "deptId", required = false) Long deptId,
			@RequestParam(value = "year",required = false) String year,

			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size) {
		Map<String, Object> response = new HashMap<>();

		if (page != null && size != null) {
			Page<StudentListDto> students = studentService.getByDeptAndColege(page, size, collegeId, deptId);
			response.put("students", students.getContent());
			response.put("currentPage", students.getNumber());
			response.put("totalItems", students.getTotalElements());
			response.put("totalPages", students.getTotalPages());
		}else{
			List<StudentListDto> students = studentService.getDeptAndCollegeWithoutFilter(collegeId, deptId,year);
			response.put("students", students);
			response.put("totalItems",students.size());
		}
		return ResponseEntity.ok(response);
	}
	@Operation(
			summary = "get Internal and External students with pagination and without pagination",
			description = "Fetches a list of all Internal and External students, with optional pagination and filtering by dept/year."
	)
	@GetMapping("/getIntorExtStudent")
	public ResponseEntity<Object>
		getInteOrExte(@RequestParam("collegeId") Long collegeId,@RequestParam(value = "page", required = false) Integer page,
				  @RequestParam(value = "size", required = false) Integer size){
		Map<String, Object> response = new HashMap<>();

		if (page != null && size != null) {
			Page<StudentListDto> internalStudent = studentService.getInternalStudent(page, size, collegeId);

			response.put("students", internalStudent.getContent());
			response.put("currentPage", internalStudent.getNumber());
			response.put("totalItems", internalStudent.getTotalElements());
			response.put("totalPages", internalStudent.getTotalPages());
		}else{
			List<StudentListDto> studentsWithoutFilter = studentService.getStudentsWithoutFilter(collegeId);
			response.put("students", studentsWithoutFilter);
			response.put("totalItems", studentsWithoutFilter.size());

		}
		return ResponseEntity.ok(response);
	}
	@GetMapping("/response")
	public ResponseEntity<Object> getStatus(@RequestParam Long id){
		
		HashMap<String, Object> response = new HashMap<>();

		Long status = userService.getStatus(id);
		response.put("status", status);
		
		return new ResponseEntity<Object>(response, HttpStatus.OK);
		
	}

	@Operation(summary = "get status of student by hod or deptIncharge")
	@GetMapping("/api/hodStatus")
	public ResponseEntity<Object> hodVerified(@RequestParam Long studentId){
		HashMap<String, Object> response = new HashMap<>();
		int verified = userService.findByHodVerified(studentId);
	
		if(!(verified==0)) {
			response.put("msg", "Verfied");
			response.put("verified", verified);
			response.put("status", 200);
			return new ResponseEntity<Object>(response, HttpStatus.OK);

		}
		else {
			response.put("msg", "Not Verfied");
			response.put("verified", verified);
			response.put("status", 400);
			return new ResponseEntity<Object>(response, HttpStatus.OK);

		}
	}
   
	@GetMapping("/industries")
	public ResponseEntity<Object> industries(){
		HashMap<String, Object> response = new HashMap<>();

		List<Company> industries = companyService.getIndustries();
		if(!industries.isEmpty()) {
			response.put("status", 200);
			response.put("industries", industries);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}
		else {
			response.put("status", 200);
			response.put("industries", industries);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
			
		}
	}


	@Operation(summary = "Save feedback from student to company for mobile app")
	@PostMapping("/api/saveFeedBack")
	public ResponseEntity<Object> giveFeedBack(@RequestBody FeedbackData feedbackData,
											   @RequestParam(value = "internshipId",required = false) Long internshipId,
	                                           @RequestParam Long studentId) {
	    try {
	        // Check if feedback already exists
	        FeedbackData feedbackData2 = feedbacklService.getFeedbackData(internshipId, studentId);
	        
	        System.out.println("feedbackffsdkfjsfksh"+feedbackData2);
	        if (feedbackData2 != null) {
	            // Feedback already given
	            return ResponseEntity.status(HttpStatus.OK)
	                                 .body(Map.of("status", "succes", "message", "Feedback already given","feedback",feedbackData2));
	        }
	        
	        // Save new feedback
	        FeedbackData savedFeedback = feedbacklService.saveFeedback(feedbackData, internshipId,studentId);
	        if (savedFeedback != null) {
	            return ResponseEntity.ok(Map.of("status", "success", "message", "Feedback saved successfully.",
	                                            "feedback", savedFeedback));
	        } else {
	            return ResponseEntity.status(HttpStatus.OK)
	                                 .body(Map.of("status", "not found", "message", "Feedback not saved"));
	        }
	     } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body(Map.of("status", "error", "message",
	                                          "An error occurred while saving feedback.", 
	                                          "error", e.getMessage()));
	    }
	}


	@Operation(summary = "get feedback from student to company for mobile app")
	@GetMapping("/api/getfeeedback")
	public ResponseEntity<Object> getFeedback(@RequestParam(value = "internshipId",required = false) Long internshipId, Long studentId){
		
        FeedbackData feedbackData2 = feedbacklService.getFeedbackData(internshipId, studentId);
        if (feedbackData2 != null) {
            return ResponseEntity.ok(Map.of("status", "success", "message", "Feedback saved successfully.",
                                            "feedback", feedbackData2));
        } else {
            return ResponseEntity.status(HttpStatus.OK)
                                 .body(Map.of("status", "not found", "message", "Feedback not saved"));
        }

	
	}

	@Operation(summary = "get feedback from company for student for mobile app")
	@GetMapping("/getFeedbackFromCompany")
	public ResponseEntity<Object> getfeedBackFromCompany(@RequestParam Long internshipId) {
	    ProgressData progressData = progresDataService.getProgressData(internshipId);

	    HashMap<String, Object> response = new HashMap<>();
	    List<Map<String, String>> feedlist = new ArrayList<>();
	    
	    if (progressData != null) {
	        String[] para1 = progressData.getParameter_1().split(",");
	        String[] para2 = progressData.getParameter_2().split(",");
	        String[] para3 = progressData.getParameter_3().split(",");
	        String[] para4 = progressData.getParameter_4().split(",");
	        String[] para5 = progressData.getParameter_5().split(",");
	        String[] para6 = progressData.getParameter_6().split(",");
	        String[] para7 = progressData.getParameter_7().split(",");
	        String[] para8 = progressData.getParameter_8().split(",");
	        String[] overall = progressData.getOverall().split(",");

	        for (int i = 0; i < para1.length; i++) {
	            Map<String, String> feedbackEntry = new LinkedHashMap<>();
	            feedbackEntry.put("key_1", para1[i].split("-")[1]);
	            feedbackEntry.put("key_2", para2[i].split("-")[1]);
	            feedbackEntry.put("key_3", para3[i].split("-")[1]);
	            feedbackEntry.put("key_4", para4[i].split("-")[1]);
	            feedbackEntry.put("key_5", para5[i].split("-")[1]);
	            feedbackEntry.put("key_6", para6[i].split("-")[1]);
	            feedbackEntry.put("key_7", para7[i].split("-")[1]);
	            feedbackEntry.put("key_8", para8[i].split("-")[1]);
	            feedbackEntry.put("overall", overall[i].split("-")[1]);

	            feedlist.add(feedbackEntry);
	        }
	       
	        response.put("feedback", feedlist);
	        response.put("comment", progressData.getComments());
          
	    }

	    if(!feedlist.isEmpty()) {
	    	  response.put("status",  "success");
	            response.put("status_Code", "200");
	            response.put("feedback", feedlist);
	            return new ResponseEntity<>(response, HttpStatus.OK);

	    }else {
	    	  response.put("status",  "notFound");
	            response.put("status_Code", "200");
	            response.put("feedback", feedlist);
	    	
	    	  return new ResponseEntity<>(response, HttpStatus.OK);
	    }

	}

	@Operation(summary = "get CollegeInfo for admin")
	@GetMapping("/collegeInfo")
	public ResponseEntity<Object> getCollegeInfo(@RequestParam String email) {
		College college = collegeService.getCollegeInfo(email);

		if (college == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("College not found for email: " + email);
		}

		Map<String, Object> response = Map.of(
		"College",college,"msg","College Received Succesfully!"
		);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/companyInfo")
	public ResponseEntity<Object> getCompanyInfo(@RequestParam String email) {
		Company company = companyService.companyInfo(email);

		if (company == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("company not found for email: " + email);
		}
		Map<String, Object> response = Map.of(
				"company",company,"msg","company Received Succesfully!"
		);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/findClj")
	public ResponseEntity<Object> getColleges(){
		List<College> colleges = collegeService.getColleges();
		if (!colleges.isEmpty()){
			return ResponseEntity.status(200).body(Map.of("colleges",colleges,"msg ","found"));
		}else{
			return ResponseEntity.status(200).body(Map.of("colleges",colleges,"msg ","not-found"));

		}
	}
	@Operation(summary = "Get manager and Lead")
	@GetMapping("/managerAndLead")
	public ResponseEntity<Object> getManagerAndLead(@RequestParam Long companyId)
	{
		List<Map<String, Object>> managerAndLead = userService.findManagerAndLead(companyId);
	    return  ResponseEntity.ok().body(managerAndLead);
	}

	@Operation(summary = "Edit Api for Admin ")
	@PostMapping("/editAdmin")
	public ResponseEntity<Object> editAdmin(@RequestParam Long adminId, @RequestBody AdminDto adminDto){
		HashMap<Object, Object> response = new HashMap<>();
		try {
			User user = userService.editAdmin(adminId, adminDto);
			response.put("msg","Admin Update Successfully");
			response.put("data",user);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("msg",e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@Operation(summary = "GET for Admin !"
	)
	@GetMapping("/getAdmin")
	public ResponseEntity<Object> getAdmin(@RequestParam Long adminId){
		HashMap<Object, Object> response = new HashMap<>();
		Optional<User> user = userRepository.findById(adminId);
		if(user.isPresent()){
			response.put("msg","Admin Got!");
			response.put("data",user);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		else{
			response.put("msg","Not found");
			return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
		}
	}
}

	




