package com.skilladmin.controller;


import java.util.stream.Stream;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skilladmin.dto.InternshipDto;
import com.skilladmin.dto.ResultDto;
import com.skilladmin.model.AppUpdate;
import com.skilladmin.model.Certificates;
import com.skilladmin.model.College;
import com.skilladmin.model.Company;
import com.skilladmin.model.Internships;
import com.skilladmin.model.NotificationRequest;
import com.skilladmin.model.Results;
import com.skilladmin.model.User;
import com.skilladmin.repository.CollegeRepository;
import com.skilladmin.repository.CompanyRepository;
import com.skilladmin.repository.InternshipRepository;
import com.skilladmin.repository.PayementHistoryRepository;
import com.skilladmin.repository.ResultRepository;
import com.skilladmin.repository.StudentRepository;
import com.skilladmin.repository.StudentRequestRepository;
import com.skilladmin.repository.UserRepository;
import com.skilladmin.service.AppUpdateService;
import com.skilladmin.service.CertificateService;
import com.skilladmin.service.InternshipService;
import com.skilladmin.service.NotificationService;
import com.skilladmin.service.ResultService;
import com.skilladmin.service.StudentService;

@RestController
@CrossOrigin(origins = {"*"})

public class
NotificationController {

	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private InternshipService internshipService;
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private CertificateService certificateService;
	
	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private UserRepository repository;
	@Autowired
	private CollegeRepository collegeRepository;
	@Autowired
	private InternshipRepository internshipRepository;
	@Autowired
	private ResultRepository resultRepository;
	@Autowired
	private ResultService resultService;
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private AppUpdateService appUpdateService;
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private StudentRequestRepository studentRequestRepository;
	@Autowired
	private PayementHistoryRepository payementHistoryRepository;
	@GetMapping("/notification")
	public ResponseEntity<Object> getNotification(@RequestParam Long receiverId)
	{
	 HashMap<Object, Object> response = new HashMap<>();
	 List<NotificationRequest> notification = notificationService.getNotification(receiverId);
	 if(!notification.isEmpty()) {
		 response.put("msg","Notifications Received Succesfully !" );
		 response.put("notification", notification);
		 return new ResponseEntity<Object>(response, HttpStatus.OK);
	 }
	 else {
		 response.put("msg"," You don't have Notifications !" );
		 return new ResponseEntity<Object>(response, HttpStatus.OK);

	 }



	}

	@PostMapping("/read")
	public ResponseEntity<List<NotificationRequest>> markAsRead(@RequestBody List<Long> ids) {
		List<NotificationRequest> updatedNotifications = notificationService.readAll(ids);
		return ResponseEntity.ok(updatedNotifications);
	}

	
	@GetMapping("/api/certification")
	public ResponseEntity<Map<String, Object>> getCertifiction(@RequestParam Long studentId) {
	    HashMap<String, Object> response = new HashMap<>();
	    Company company=null;
	    User user=null;
	    String datestarted=null;
	    String dateCompleted=null;
	    try {
	        // Fetch the certificate data
	        List<InternshipDto> certificateData = studentService.getCertificate(studentId);

	      

	        response.put("msg", "Certificate data retrieved successfully");
	        response.put("data", certificateData);

	        for(InternshipDto certificate : certificateData) {
	        	 company = companyRepository.findById(certificate.getCompanyId()).get();
	        	 user = repository.findByEmailAndRole("2",company.getEmail_id() );
	        	  datestarted = certificate.getDateStarted().toString();
	        	  dateCompleted=  certificate.getDateCompleted().toString();
	        }
	        
	        response.put("dateCompleted", dateCompleted);
	        response.put("datestarted", datestarted);
	        response.put("signature",user.getSignature() );
	        response.put("logo",company.getLogo() );
	        return ResponseEntity.ok(response);

	    } catch (NoSuchElementException e) {
	        response.put("msg", e.getMessage());
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	    } catch (Exception e) {
	        response.put("msg", "An error occurred: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

	@GetMapping("/api/downloadCertificate")
	public ResponseEntity<Object> getCertificateToken(@RequestParam Long studentId){
		HashMap<Object, Object> response = new HashMap<>();
//        List<Internships> certificateData = internshipRepository.getDownoadurl(studentId);

		List<Certificates> certificate = certificateService.getUrl(studentId);
		
        ArrayList<Object> arrayList = new ArrayList<>();

        if (certificate.isEmpty()) {
            response.put("message", "No certificates found for the given student ID.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        for (Certificates certificat : certificate) {
        	String downloadUrl = certificat.getUrl();
        	arrayList.add(downloadUrl);
        }
        response.put("downloadUrl", arrayList);
        response.put("message", "Certificates updated successfully.");
        return ResponseEntity.ok(response);
	}
	
	@GetMapping("/testHistory")
	public ResponseEntity<Object> getTestHistory(@RequestParam Long studentId){
		HashMap<Object, Object> response = new HashMap<>();
		List<ResultDto> testHistory = resultService.getTestHistory(studentId);
		if(!testHistory.isEmpty()) {
			response.put("status", 200);

            response.put("message", "found");
            response.put("testHistory", testHistory);
            return new ResponseEntity<Object>(response,HttpStatus.OK);

		}
		else {
			response.put("message", "notFound");
			response.put("status", 200);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
	}
	
	@GetMapping("/deleteUrl")
	public ResponseEntity<Object> userDelete(){
		
		return ResponseEntity.status(HttpStatus.OK).body(Map.of("Success", "200", 
			"deleteUrl","https://home.skillpilots.com/home"));
	}
	
	@GetMapping("/checkVersion")
	public ResponseEntity<Object> checkVersionCode(){

       List<AppUpdate> appUpdates = appUpdateService.findByVersionId()		;
     Stream<String> list = appUpdates.stream().map(AppUpdate::getVersionCode);

     return ResponseEntity.status(HttpStatus.OK).body(Map.of("vesionCode",list,"status","200"));
     
     
       
	}
	
	@PatchMapping("/logout")
	public ResponseEntity<Object> logOut(@RequestParam Long id){
		
		User user = userRepository.findById(id).get();
		user.setNotificationToken("0");
		userRepository.save(user);
		return ResponseEntity.status(HttpStatus.OK).body(Map.of("msg", "Log Out Succesfull"));
		
	}
	
	
	//  admin dashbord api for graph
	@GetMapping("/getCounts")
	public ResponseEntity<Map<String, Object>> getCounts() {
	    long collegeCount = collegeRepository.count();
	    long companyCount = companyRepository.count();
	    Long externalStudentCount = Objects.requireNonNullElse(userRepository.findAllExternalStudentCount(), 0L);
	    Long internalStudentCount = Objects.requireNonNullElse(userRepository.findAllInternalStudentCount(), 0L);

	    // Weekly labels
	    List<String> weeklyLabels = Arrays.asList("M", "T", "W", "T", "F", "S", "S");
        List<Long> registrations = new ArrayList<>();
    	
	    // Get current date and find start of the week (Monday)
	    LocalDate today = LocalDate.now();
	    LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));


	    for (int i = 0; i < 7; i++) {
            LocalDate date = startOfWeek.plusDays(i);
            Date convertedDate = convertLocalDateToDate(date);  // Convert LocalDate to Date

            
            Long count = studentRepository.countStudentsRegisteredOnDate(convertedDate);
            registrations.add(count != null ? count : 0);
        }
	    // Monthly labels and counts (Ordered from Jan → Dec)
	    List<String> monthlyLabels = new ArrayList<>();
	    List<Long> monthlyCounts = new ArrayList<>();
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(Calendar.MONTH, Calendar.JANUARY); // Start from January

	    for (int i = 0; i < 12; i++) {
	        Date dateForQuery = calendar.getTime();

	        Long count = 0L;
	        try {
	          
	        	count = Objects.requireNonNullElse(studentRequestRepository.countRequestsInMonth(dateForQuery), 0L);

	        } catch (Exception e) {
	            System.err.println("Error fetching data for date: " + dateForQuery);
	            e.printStackTrace();
	        }

	        monthlyLabels.add(calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH) + " " + calendar.get(Calendar.YEAR));
	        monthlyCounts.add(count);
	        

	        calendar.add(Calendar.MONTH, 1); // Move to the next month
	    }
	    List<String> monthlyPaymentLabels = new ArrayList<>(monthlyLabels);
	    List<Long> monthlyPayments = new ArrayList<>();
	    int currentYear = Year.now().getValue();

	    for (int i = 0; i < 12; i++) {
	        int month = calendar.get(Calendar.MONTH) + 1; // Get month (1-based)
	        
	        // Fetch total amount for the given month and year
	        Long totalAmount = payementHistoryRepository.getTotalPaidAmountByMonth(currentYear, month);

	        monthlyLabels.add(calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH) + " " + currentYear);
	        monthlyPayments.add(totalAmount);

	        calendar.add(Calendar.MONTH, 1); // Move to next month
	    }

	    // Prepare final response using LinkedHashMap to maintain order
	    Map<String, Object> response = new LinkedHashMap<>();
	    response.put("collegeCount", collegeCount);
	    response.put("companyCount", companyCount);
	    response.put("externalStudentCount", externalStudentCount);
	    response.put("internalStudentCount", internalStudentCount);
	    response.put("studentList", studentRepository.getLastStudents());
	    response.put("monthlyPayments", Map.of("labels", monthlyPaymentLabels, "data", monthlyPayments));

	    // Weekly dataset
	    Map<String, Object> weeklyData = new LinkedHashMap<>();
	    weeklyData.put("labels", weeklyLabels);
	    weeklyData.put("data", registrations);
	    response.put("weeklyRegistrations", weeklyData);

	    // Monthly dataset
	    Map<String, Object> monthlyData = new LinkedHashMap<>();
	    monthlyData.put("labels", monthlyLabels);
	    monthlyData.put("data", monthlyCounts);
	    response.put("studentMonthlyRequests", monthlyData);

	    return ResponseEntity.ok(response);
	}
	     private Date convertLocalDateToDate(LocalDate localDate) {
	         return java.sql.Date.valueOf(localDate);
	     }
}
