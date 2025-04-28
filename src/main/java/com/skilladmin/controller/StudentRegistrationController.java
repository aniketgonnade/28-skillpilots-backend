package com.skilladmin.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import com.skilladmin.model.*;
import com.skilladmin.repository.UserTestPreferenceRepo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.skilladmin.config.StudentDto;
import com.skilladmin.dto.RegistrationRequestDto;
import com.skilladmin.dto.StudentUpdateDto;
import com.skilladmin.exception.UserNotFoundException;
import com.skilladmin.repository.StudentPastRepository;
import com.skilladmin.repository.UserRepository;
import com.skilladmin.service.DepartmentService;
import com.skilladmin.service.StudentService;
import com.skilladmin.service.UserService;
import com.skilladmin.util.ProjectConstants;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = { "*" })

public class StudentRegistrationController {

	@Autowired
	private UserService userService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private StudentPastRepository studentPastRepository;
	@Autowired
	private DepartmentService departmentService;
    @Autowired
	private UserTestPreferenceRepo userTestPreferenceRepo;

	@PostMapping("/register")
	public ResponseEntity<Object> stepOneRegister(@RequestBody User user) {
		HashMap<String, Object> response = new HashMap<>();

		try {
			// Check if the email already exists
			if (userService.emailExists(user.getEmail())) {
				response.put("message", "Email is already registered.");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			}

			Random random = new Random();

			user.setRole(ProjectConstants.default_student);
			int otpValue = random.nextInt(1000000);
			String otp = String.format("%06d", otpValue);
			user.setOtp(otp.substring(0, 4));

			userService.saveUser(user);

			Student student = new Student();
			student.setEmail_id(user.getEmail());
			student.setStudent_id(user.getId());
			studentService.saveStudent(student);


			String body = "<h1 style=\"font-size:55px;margin:20px;text-align:center\">Welcome to <span style=\"color:#1974c3;\">Skill<span style=\"color:#01af36;\">Pilots</span> </h1>"
					+ "<p style=\"text-align:center\"> Thank You For Registering! Here is your registration code - <strong>"
					+ otp.substring(0, 4) + "</strong></p><p style=\"text-align:center\"> Please follow the link"
					+ "<br>Click <a href=''><strong>here</strong></a> to complete your registration."
					+ "<br>You'll be asked to change your password for security reasons.<br></p>"
					+ ProjectConstants.mail_footer;

			userService.sendVerificationEmail(user.getEmail(), "Registration Code & Link for SkillPilots", body);

			response.put("message", "User registered successfully. Verification email sent.");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("message", "An error occurred during registration.");
			response.put("error", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/setPassword")
	public ResponseEntity<Object> setPassword(@RequestBody User user) {
		HashMap<String, Object> response = new HashMap<>();
		User existingUser = userRepository.findByEmail(user.getEmail());

		if (existingUser == null) {
			response.put("message", "User not found.");
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
		if (!existingUser.getOtp().equals(user.getOtp())) {
			response.put("message", "Invalid OTP.");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		existingUser.setVerified(1);
		existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
		existingUser.setPlainPassword(user.getPassword());
		userRepository.save(existingUser);

		response.put("message", "Password set successfully.");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/student")
	public ResponseEntity<StudentDto> registerStudent(@RequestBody RegistrationRequestDto registrationRequest,
			@RequestParam Long id, @RequestParam Long department, @RequestParam int collegeId,
			@RequestParam String role) {
		StudentDto response = studentService.register(registrationRequest.getStudent(), role, collegeId,
				registrationRequest.getUser(), registrationRequest.getStudentPast(), id, department);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/api/edit")
	public ResponseEntity<Student> editStudent(@RequestParam Long student_id, @RequestBody Student student) {

		// Call the service method to edit the student
		Student updatedStudent = studentService.editStudent(student, student_id);

		if (updatedStudent != null) {
			return ResponseEntity.ok(updatedStudent);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/api/upload")
	public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file, Long student_id) {
		if (file.isEmpty()) {
			return new ResponseEntity<>("File is empty", HttpStatus.BAD_REQUEST);
		}
		Student student = studentService.addPhoto(student_id, file);
		if (student != null) {
			return ResponseEntity.ok("File uploaded successfully: " + file.getOriginalFilename());
		} else {
			return new ResponseEntity<>("File upload failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/api/images")
	public ResponseEntity<Resource> getImage(@RequestParam Long studentId) {
		byte[] imageBytes = studentService.getPhoto(studentId);
		if (imageBytes == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		ByteArrayResource resource = new ByteArrayResource(imageBytes);
		return ResponseEntity.ok().contentLength(imageBytes.length)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"image.jpg\"").body(resource);
	}

	@GetMapping("/api/student")
	public ResponseEntity<Object> getStudent(@RequestParam Long studentId) {
		Map<String, Object> student = studentService.getStudent(studentId);
		return ResponseEntity.ok().body(student);
	}

	@PutMapping("/api/changePass")
	public ResponseEntity<Object> changepassword(@RequestParam Long studentId, @RequestParam String password,
			@RequestParam String newPass) {
		HashMap<Object, Object> response = new HashMap<>();

//		User changePassword = studentService.changePassword(studentId, password, newPass);
//
//		if (changePassword != null) {
//			response.put("msg", "Password Update Succesfully");
//			response.put("statusCode", 200);
//			return new ResponseEntity<>(response, HttpStatus.OK);
//		} else {
//			response.put("msg", "Password change failed. Check your credentials and try again.");
//			response.put("statusCode", HttpStatus.BAD_REQUEST);
//			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//
//		}
		Optional<User> userOptional = userRepository.findById(studentId);
	    if (!userOptional.isPresent()) {
	        return null;
	    }

	    User existingUser = userOptional.get();

	    try {
	        if (passwordEncoder.matches(password, existingUser.getPassword())|| existingUser.getPassword().equals(password)) {
	            existingUser.setPassword(passwordEncoder.encode(newPass));
	            existingUser.setPlainPassword(newPass);
	            userRepository.save(existingUser);
	            response.put("msg", "Password Update Succesfully");
				response.put("statusCode", 200);	    
				return new ResponseEntity<>(response, HttpStatus.OK);
	        
	        } else {
	        	response.put("msg", "old Password Not Match");
				response.put("statusCode", 200);	    
				return new ResponseEntity<>(response, HttpStatus.OK);
	        }
	    } catch (Exception e) {
	    	response.put("msg", "Password change failed. Check your credentials and try again.");
		response.put("statusCode", HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    }
		
		
	}

	@PutMapping("/getOtp")
	public ResponseEntity<Object> sentOtp(@RequestParam String email) {
		HashMap<Object, Object> response = new HashMap<>();
		try {
			User otp = studentService.forgotPassword(email);
			response.put("msg", "Otp sent successfully..");
			response.put("otp", otp.getOtp());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (UserNotFoundException e) {
			response.put("msg", e.getMessage());
			response.put("status", 404);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/resetPassword")
	public ResponseEntity<?> resetPass(@RequestParam String email, @RequestParam String otp,
			@RequestParam String newPass) {
		HashMap<Object, Object> response = new HashMap<>();

		try {
			User existingUser = userRepository.findByEmail(email);

			if (otp.equals(existingUser.getOtp())) {
				existingUser.setPassword(passwordEncoder.encode(newPass));
				existingUser.setPlainPassword(newPass);
				userRepository.save(existingUser);
				response.put("msg", "Password reset successfully");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.put("msg", "OTP not matching");
				response.put("statusCode", 400);
				return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
			}
		} catch (UserNotFoundException e) {
			response.put("msg", "User not found");
			response.put("status", 404);
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/editStudentPast")
	public ResponseEntity<Object> editStudentPast(@RequestParam Long studentId, @RequestBody StudentPast studentPast) {
		HashMap<String, Object> response = new HashMap<>();

		try {
			StudentPast editPast = studentService.editPast(studentId, studentPast);
			response.put("status", "success");
			response.put("data", editPast);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.put("status", "error");
			response.put("message", e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/educationDetails")
	public ResponseEntity<Object> getEducation(@RequestParam Long studentId) {
		HashMap<String, Object> response = new HashMap<>();
		List<Object[]> editEducation = studentPastRepository.editEducation(studentId);

		if (editEducation.isEmpty()) {
			response.put("status", 200);
			response.put("message", "Student education details not found.");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			System.out.println("student " + editEducation);

			List<HashMap<String, Object>> educationDetailsList = new ArrayList<>();

			for (Object[] data : editEducation) {
				// Debug prints for each element in the data array
				System.out.println("Data[0]: " + data[0]);
				System.out.println("Data[1]: " + data[1]);

				StudentPast studentPast = (StudentPast) data[0];
				String currYear = (String) data[1];
				Long deptId = studentPast.getDepartment();

				Optional<Department> department = departmentService.findById(deptId);
				HashMap<String, Object> educationDetails = new HashMap<>();
				educationDetails.put("educationDetails", studentPast);
				educationDetails.put("currYear", currYear);

				if (department.isPresent()) {
					Department department2 = department.get();
					educationDetails.put("department", department2.getDept_name());
				} else {
					// Include a placeholder or error message for the missing department
					educationDetails.put("department", "Department not found");
				}

				educationDetailsList.add(educationDetails);
			}

			response.put("status", 200);
			response.put("message", "Student education details found.");
			response.put("educationDetailsList", educationDetailsList);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@PutMapping("/updateStudentAndPast")
	public ResponseEntity<Object> updateStudentAndPast(@RequestParam Long studentId, @RequestBody StudentUpdateDto dto,
			@RequestParam Long studpastId) {
		studentService.updateStudentAndPast(studentId, dto, studpastId);
		HashMap<String, Object> response = new HashMap<>();
		response.put("message", "Student and StudentPast updated successfully");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/api/addpast")
	public ResponseEntity<Object> addStudentPast(@RequestParam Long studentId, @RequestBody StudentUpdateDto dto) {
		HashMap<Object, Object> response = new HashMap<>();

		response.put("msg", "data add succesfully");
		response.put("studentPast", studentService.addStudentPast(studentId, dto));

		return new ResponseEntity<>(response, HttpStatus.OK);

	}
	


	
	
	// 1ST STEP STUDENT
	@GetMapping("/defaultRole")
    public ResponseEntity<List<Map<String, Object>>> getUsersWithRole20()
	{
        List<Map<String, Object>> users = userService.getUsersWithRole20();
        if (users.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(users);
        }
        return ResponseEntity.ok(users);
    }
	
	
	
	@GetMapping("/export/excel")
	public void exportUsersToExcel(HttpServletResponse response) throws IOException, java.io.IOException {
	    // Set response headers for Excel file
	    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	    response.setHeader("Content-Disposition", "attachment; filename=users.xlsx");
	    // Fetch data from repository
	    List<Object[]> users = userRepository.findUsersWithRole20();
	    // Create Excel workbook and sheet
	    Workbook workbook = new XSSFWorkbook();
	    Sheet sheet = workbook.createSheet("Users");
	    // Define Header Row
	    String[] columns = {"Username", "Email", "Gender", "Contact No", "Role"};
	    Row headerRow = sheet.createRow(0);
	    for (int i = 0; i < columns.length; i++) {
	        Cell cell = headerRow.createCell(i);
	        cell.setCellValue(columns[i]);
	    }
	    // Populate data rows
	    int rowNum = 1;
	    for (Object[] user : users) {
	        Row row = sheet.createRow(rowNum++);
	        // Handle null values using a helper method
	        row.createCell(0).setCellValue(getStringValue(user[0])); // Username
	        row.createCell(1).setCellValue(getStringValue(user[1])); // Email
	        row.createCell(2).setCellValue(getStringValue(user[2])); // Gender
	        row.createCell(3).setCellValue(getStringValue(user[3])); // Contact No
	        row.createCell(4).setCellValue(getStringValue(user[4])); // Role
	    }
	    // Auto-size columns
	    for (int i = 0; i < columns.length; i++) {
	        sheet.autoSizeColumn(i);
	    }
	    // Write workbook to response output stream
	    workbook.write(response.getOutputStream());
	    workbook.close();
	}
	// Helper method to handle null values
	private String getStringValue(Object obj) {
	    return (obj == null) ? "" : obj.toString();
	}
}
