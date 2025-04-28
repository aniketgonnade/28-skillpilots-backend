package com.skilladmin.controller;


import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import com.skilladmin.config.StudentDto;
import com.skilladmin.dto.AdvertisementFilter;
import com.skilladmin.dto.TestSubmissionRequest;
import com.skilladmin.enumclass.QuestionType;
import com.skilladmin.model.AssessmentTest;
import com.skilladmin.model.College;
import com.skilladmin.model.CompAdvertisement;
import com.skilladmin.model.Department;
import com.skilladmin.model.Questions;
import com.skilladmin.model.Results;
import com.skilladmin.model.Student;
import com.skilladmin.repository.CollegeRepository;
import com.skilladmin.repository.StudentRepository;
import com.skilladmin.service.AssesmentService;
import com.skilladmin.service.CompAdvertismentService;
import com.skilladmin.service.DepartmentService;
import com.skilladmin.service.QuestionService;
import com.skilladmin.service.ResultService;
import com.skilladmin.service.StudentService;
import com.skilladmin.service.UserService;





@RestController
@CrossOrigin(origins = {"*"})

public class LoginController {

	@Autowired
	private StudentService studentService;
	@Autowired
	private StudentRepository studentRepository;
    @Autowired
	private CompAdvertismentService advertismentService;
    @Autowired
    private CollegeRepository collegeRepository;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private UserService userService;
    @Autowired
    private AssesmentService assesmentService;
    @Autowired
    private QuestionService questionService;
	@Autowired
    private ResultService resultService;

	@PostMapping("/signin")
	public ResponseEntity<StudentDto>signIn(@RequestBody StudentDto signInRequest){
		return ResponseEntity.ok(studentService.login(signInRequest));
	}
	
	@GetMapping("/api/get")
	public Student getUser(@RequestParam Long id){
		return  studentRepository.findById(id).stream().findAny().get();
		
	}
	
	@GetMapping("/api/advertisement")
	public ResponseEntity<Object> getAllAdvertisemnt(){
		HashMap<Object, Object> response = new HashMap<>();
		List<CompAdvertisement> allAdvertisemnts = advertismentService.getAllAdvertisemnts();
	if(!allAdvertisemnts.isEmpty()) {
		response.put("msg", "Advertisemnt Get");
		response.put("advertisement", allAdvertisemnts);
		return  new ResponseEntity<Object>(response, HttpStatus.OK);
	}
	else {
		response.put("msg", "Not Found");
		return  new ResponseEntity<Object>(response, HttpStatus.NO_CONTENT);

	}
	}


	@GetMapping("/api/colleges")
	public ResponseEntity<Object> getColleges(){
		HashMap<Object, Object> response = new HashMap<>();
		List<College> colleges = collegeRepository.findAll();
		if(!colleges.isEmpty()) {
			response.put("msg", "Colleges");
			response.put("Colleges", colleges);
			return  new ResponseEntity<Object>(response, HttpStatus.OK);

		}
		else {
			response.put("msg", "Not Found");
			return  new ResponseEntity<Object>(response, HttpStatus.NO_CONTENT);
		}
	}

	@GetMapping("/api/departments")
	public ResponseEntity<Object> getDepartments(){
		HashMap<Object, Object> response = new HashMap<>();
		List<Department> allDept = departmentService.getDepartments();
		if(!allDept.isEmpty()) {
			response.put("msg", "allDept Get");
			response.put("allDept", allDept);
			return  new ResponseEntity<Object>(response, HttpStatus.OK);
		}
		else {
			response.put("msg", "Not Found");
			return  new ResponseEntity<Object>(response, HttpStatus.NO_CONTENT);

		}
	
	}

	
	 @GetMapping("/checkExistsUser")
	    public ResponseEntity<Object> checkExistsUser(@RequestParam String email) {
			HashMap<Object, Object> response = new HashMap<>();
        	StudentDto dto= new StudentDto();

		 boolean emailExists = userService.emailExists(email);
	        if (emailExists) {
	        	dto.setStatusCode(200);
	        	dto.setExists(emailExists);
	        	response.put("response", dto);
	            return new ResponseEntity<>(response, HttpStatus.OK);

	        } else {
	        	dto.setStatusCode(404);
	        	dto.setMessage("User Not Found");
	        	response.put("response", dto);
	            return new ResponseEntity<>(response, HttpStatus.OK);

	        }
	    }
	
	 @PostMapping("/api/filter")
	 public ResponseEntity<HashMap<String, Object>> getFilteredAdvertisements(@RequestBody AdvertisementFilter filter) {
	     HashMap<String, Object> response = new HashMap<>();
	     Boolean isPaid = null;

	   
	     if (filter.getStipend() != null) {
	         isPaid = filter.getStipend().equalsIgnoreCase("paid");
	     } else {
	         isPaid = false; 
	     }
	     
	     // Print for debugging
	     System.out.println("Location parameter: " + filter.getLocation());
	     System.out.println("Technology parameter: " + filter.getTechnology());
	     System.out.println("IsPaid parameter: " + isPaid);
	     System.out.println("Duration parameter: " + filter.getDuration());

	     
	     // Fetch advertisements !=nu the service
	     List<CompAdvertisement> advertisements = advertismentService.getFilter(filter.getLocation(), filter.getTechnology(), isPaid, filter.getDuration());

	     // Print for debugging
	     System.out.println("Filtered advertisements: " + advertisements);

	     // Prepare response
	     if (advertisements != null && !advertisements.isEmpty()) {
	         response.put("statusCode", 200);
	         response.put("advertisements", advertisements);
	     } else {
	         response.put("statusCode", 404);
	         response.put("msg", "No advertisements found");
	     }
	     return ResponseEntity.ok(response);
	 }



	 @GetMapping("/api/tests")
	 public ResponseEntity<Object> getAssesments(){
		 HashMap<Object, Object>response  = new HashMap<>();

		 List<AssessmentTest> findAsssesments = assesmentService.findAsssesments();
	 
		 if(!findAsssesments.isEmpty()) {
			 response.put("statusCode", 200);

			 response.put("msg", "Asessements");

			 response.put("Asessements", findAsssesments);

			 return new ResponseEntity<Object>(response, HttpStatus.OK);
		 }
		 else {
			 response.put("msg", "Not Found");
			 return new ResponseEntity<Object>(response, HttpStatus.NO_CONTENT);

		 }
	 
	 }

	 @GetMapping("/api/startTest")
	 public ResponseEntity<Object> startTest(@RequestParam Long testId) {
	     HashMap<Object, Object> response = new HashMap<>();
	     AssessmentTest findTests = assesmentService.findTests(testId);

	     if (findTests == null) {
			 response.put("statusCode", 400);

	         response.put("message", "Test not found");
	         return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	     }

	     List<Questions> question = questionService.getQuestionByTestId(testId);
		 Collections.shuffle(question);
		 int limit = Math.min(findTests.getTotal_que(), question.size());
		 List<Questions> limitedQuestions = question.subList(0, limit);
	//	 model.addAttribute("questions", limitedQuestions);
		  response.put("statusCode", 200);

	     response.put("test", findTests);
	     response.put("questions", limitedQuestions);
	     return new ResponseEntity<>(response, HttpStatus.OK);
	 }

	
	 @RequestMapping(value = "/submitTest", method = RequestMethod.POST)
	    public Results submitTest(@RequestBody TestSubmissionRequest submissionRequest) {

	        Long studentId = submissionRequest.getStudentId();
	        Long testId = submissionRequest.getTestId();
	        Map<String, String> allAnswers = submissionRequest.getAnswers();
	        boolean disqualified = submissionRequest.isDisqualified();

	        System.out.println("Student ID: " + studentId);
	        System.out.println("Test ID: " + testId);
	        System.out.println("All Answers: " + allAnswers);

	        Results results = new Results(); 

	        // Fetch the test using the testId
	        AssessmentTest test = assesmentService.findByTestId(testId);
	        if (test == null) {
	            System.out.println("Test not found!");
	            return results;
	        }

	        // Handle disqualification
	        if (disqualified) {
	            results.setStudentId(studentId);
	            results.setResultStatus("Disqualified");
	            results.setTestId(testId);
	            resultService.saveResult(results);
	            return results;
	        }

	        int totalQuestions = test.getQuestions().size();
	        int correctAnswers = 0;
	        int wrongAnswers = 0;
	        int unanswered = 0;


	        // Process each answer
	        for (Map.Entry<String, String> entry : allAnswers.entrySet()) {
	            String key = entry.getKey();
	            String selectedOption = entry.getValue();

	            
	            
	           

	            String questionIdStr = key.split("_")[1].replace("]", "");

	            try {
	                Long questionId = Long.parseLong(questionIdStr);

	                Questions question = test.getQuestions()
	                        .stream()
	                        .filter(q -> q.getId().equals(questionId))
	                        .findFirst()
	                        .orElse(null);

	                
	                if (question != null) {
	           
	                    if (selectedOption == null || selectedOption.isEmpty()) {
	                        unanswered++;
	                    } else if (question.getType().equals(QuestionType.MULTIPLE_CHOICE)) {
	                    	
	                    	System.out.println("Multiple cjoicecssssss"+question.getCorrectAnswers());
	                        List<String> correctAnswersList = question.getCorrectAnswers();
	                        if (correctAnswersList.contains(selectedOption)) {
	                            correctAnswers++;
	                        } 
	                    } else if (selectedOption.equals(question.getCorrectAnswer())) {
	                        correctAnswers++;
	                    }else {
	                        wrongAnswers++;
	                    }
	                } else {
	                    System.out.println("Question not found for ID: " + questionId);
	                }
	            } catch (NumberFormatException e) {
	                System.err.println("Invalid question ID: " + questionIdStr);
	            }
	        }

	        // Calculate total and obtained marks
	        int marksPerQuestion = test.getMark_per_que();
		 int totalMarks = correctAnswers * test.getMark_per_que();
	        int marksObtained = correctAnswers * marksPerQuestion;



		 String resultStatus = calculateResultStatus(totalMarks, test.getCut_off());

	        results.setStudentId(studentId);
	        results.setMarks(marksObtained);
	        results.setTotalMarks(totalMarks);
	        results.setCorrectAns(correctAnswers);
	        results.setWrongAns(wrongAnswers);
	        results.setNoAns(unanswered);
	        results.setOutof(test.getTotal_mark());
	        results.setResultStatus(resultStatus);
	        results.setTestId(testId);

	        System.out.println("Saving results: " + results);

	        resultService.saveResult(results);


	        return results;
	    }

	private String calculateResultStatus(int totalMarks, int cutOff) {
		return totalMarks >= cutOff ? "Pass" : "Fail";
	}



}
;