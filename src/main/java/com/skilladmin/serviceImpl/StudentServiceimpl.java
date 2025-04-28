package com.skilladmin.serviceImpl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.skilladmin.dto.*;
import com.skilladmin.model.*;
import com.skilladmin.repository.*;
import com.skilladmin.service.EmailService;
import com.skilladmin.util.FirebaseService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.skilladmin.config.JwtUtils;
import com.skilladmin.config.StudentDto;
import com.skilladmin.exception.UserNotFoundException;
import com.skilladmin.service.StudentService;
import com.skilladmin.service.UserService;
import com.skilladmin.util.ProjectConstants;


@Service
public class StudentServiceimpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private StudentPastRepository studentPastRepository;
    @Autowired
    private StudentRequestRepository studentRequestRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CollegeRepository collegeRepository;
    @Autowired
    private CollegeInternalReqRepository collegeInternalReqRepository;
    @Autowired
    private ExternalRepository externalRepository;
    @Autowired
    private InternshipRepository internshipRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private UserRepository repository;

    private TutorRepository tutorRepository;
    @Autowired
    private TrainingUserRepository trainingUserRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private RecruitmentStatusRepo recruitmentStatusRepo;
    @Autowired
    private RecruitmentRepository recruitmentRepository;
    @Autowired
    private FirebaseService firebaseService;
    @Autowired
    private CompanyDriveRepository companyDriveRepository;
    @Autowired
    private  UserTestPreferenceRepo userTestPreferenceRepo;

    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public StudentDto login(StudentDto studentDto) {
        StudentDto response = new StudentDto();

        try {
            // Find user by email
            User user = userRepository.findByEmail(studentDto.getEmail());
            if (user == null) {
                throw new UsernameNotFoundException("User not found");

            } else {

            }


            String providedPassword = studentDto.getPassword();
            String storedPassword = user.getPassword();

            TrainingUsers trainingUsers = trainingUserRepository.findByStudentId(user.getId());
            if (trainingUsers != null) {
                trainingUsers.setNotificationToken(studentDto.getNotificationToken());
                trainingUserRepository.save(trainingUsers);
            }

            user.setNotificationToken(studentDto.getNotificationToken());
            repository.save(user);


            // Check if the stored password is bcrypt-hashed
            if (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$") || storedPassword.startsWith("$2y$")) {
                // Authenticate using bcrypt password comparison
                if (!BCrypt.checkpw(providedPassword, storedPassword)) {
                    throw new BadCredentialsException("Invalid email or password");
                }
            } else {
                // Authenticate using plain text comparison
                if (!storedPassword.equals(providedPassword)) {
                    throw new BadCredentialsException("Invalid email or password");
                }
            }

            // Generate JWT token
            String jwt = jwtUtils.generateToken(user);


            UserTestPreference student = userTestPreferenceRepo.getStudent(user.getId());

            if (student == null) {
                // Create new entry if student doesn't exist
                UserTestPreference userTestPreference = new UserTestPreference();
                userTestPreference.setStudentId(user.getId());
                userTestPreference.setLoginStatus(true);
                userTestPreferenceRepo.save(userTestPreference);
                response.setLoginStatus(userTestPreference.isLoginStatus());
            } else {
                // Update existing student's login status
                //student.setLoginStatus(true);
                userTestPreferenceRepo.save(student); // Update existing record
                response.setLoginStatus(student.isLoginStatus());
            }

//	        user.setNotificationToken(st);

            // Set response data
            response.setToken(jwt);
            response.setStatusCode(200);
            response.setMessage("User logged in successfully");
            response.setUser(user);
            response.setContact(String.valueOf(user.getContact_no()));
            response.setTrainingUsers(trainingUsers);
        } catch (BadCredentialsException e) {
            // Handle bad credentials specifically
            response.setStatusCode(401);
            response.setMessage("Invalid email or password");
        } catch (UsernameNotFoundException e) {
            // Handle user not found
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            // Handle general exceptions
            response.setStatusCode(500);
            response.setMessage("An error occurred: " + e.getMessage());
        }
        return response;
    }


    @Override
    public StudentDto register(Student student, String role, int collegeId, User user, StudentPast studentPast, Long id, Long department) {
        StudentDto response = new StudentDto();

        // Retrieve the user by id
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new NullPointerException("User Not found");
        }

        User existingUser = userOptional.get();

        // Set data for the user
        existingUser.setUsername(student.getStudent_name());
        existingUser.setGender(user.getGender());
        existingUser.setCollege_id(collegeId);
        existingUser.setCreation_date(LocalDate.now());
        existingUser.setDepartment(department); // Adjust as per your requirements
        existingUser.setRole(role);
        existingUser.setType((short) 1);

        // Save the updated user entity
        User savedUser = userService.saveUser(existingUser);

        // Check if the student exists by the saved user's ID
        Optional<Student> studentOptional = studentRepository.findById(savedUser.getId());
        Student savedStudent;
        if (studentOptional.isPresent()) {
            // If student exists, update the student's information
            savedStudent = studentOptional.get();
        } else {
            // If student does not exist, create a new student entity
            savedStudent = new Student();
            savedStudent.setStudent_id(savedUser.getId());
        }

        // Set student details
        savedStudent.setStudent_name(student.getStudent_name());
        // savedStudent.setCurr_year(student.getCurr_year());
        savedStudent.setCreation_date(new Date());
        savedStudent.setD_o_b(student.getD_o_b());
        savedStudent.setEmail_id(student.getEmail_id());
        savedStudent.setContact(student.getContact());
        savedStudent.setPhoto(student.getPhoto());
        savedStudent.setHome_city(student.getHome_city());
        savedStudent.setCurr_city(student.getCurr_city());
        savedStudent.setCurr_enroll_no(student.getCurr_enroll_no());
        savedStudent.setValid_till(student.getValid_till());
        savedStudent.setSkills(student.getSkills());
        savedStudent.setInterest(student.getInterest());
        savedStudent.setHobbies(student.getHobbies());
        savedStudent.setAchievements(student.getAchievements());

        // Save the student entity
        savedStudent = studentRepository.save(savedStudent);

        // Update the saved user entity with the student name, if needed
        savedUser.setUsername(savedStudent.getStudent_name());
        userService.saveUser(savedUser);

        // Save StudentPast entity if the role is "0"
        if ("0".equals(role)) {
            studentPast.setStudent_id(savedStudent.getStudent_id());
            studentPast.setDepartment(department);
            studentPast.setCreation_date(new Date());
            savedStudent.setCollege_id((long) collegeId);
            savedStudent.setDept(String.valueOf(department));
            studentPast.setStart_date(student.getCurr_year());
            Optional<College> collegeOptional = collegeRepository.findById((long) collegeId);
            String body = "<h1 style=\"font-size:55px;margin:20px;text-align:center\">Welcome to <span style=\"color:#1974c3;\">Skill<span style=\"color:#01af36;\">Pilots</span></h1>" + "<p style=\"text-align:center\">New Student Registered.!! Please check In Skillpilot - <strong>" + "<p style=\"text-align:center\">Please Follow the link<br>" + "Click <a href=\"https://www.skillpilots.com\"><strong>here</strong></a>"

                    + ProjectConstants.mail_footer;

            if (collegeOptional.isPresent()) {
                College college = collegeOptional.get();
                studentPast.setOrganization(college.getCollege_name());
                userService.sendVerificationEmail(college.getEmail_id(), "New Notification From SkilPilot", body);
                List<User> collegeUser = userRepository.findByEmailIdCommonForNotification(college.getEmail_id(), department);
                for (User hodOrCordinator : collegeUser) {
                    userService.sendVerificationEmail(hodOrCordinator.getEmail(), "New Notification From SkilPilot", body);

                }

            }
            studentPastRepository.save(studentPast);

            // Save the updated student entity
            savedStudent = studentRepository.save(savedStudent);
        }

        // Prepare response DTO
        response.setUser(savedUser);
        response.setStudent(savedStudent);
        response.setMessage("Registration successful");
        response.setStatusCode(200); // or any appropriate status code
        response.setEmail(savedUser.getEmail());
        response.setPassword(savedUser.getPassword());
        response.setStudentPast(studentPast);

        return response;
    }

    @Override
    public StudentRequest applyRequest(StudentRequest studentRequest) {
        return studentRequestRepository.save(studentRequest);
    }

    @Override
    public List<StudentRequest> findRequest(Long request_id) {
        return studentRequestRepository.getStudentRequest(request_id);
    }

    @Override
    public Student editStudent(Student student, Long student_id) {
        // Find existing User
        Optional<User> u = userRepository.findById(student_id);
        if (u.isPresent()) {
            User existingUser = u.get();
            if (student.getStudent_name() != null) {
                existingUser.setUsername(student.getStudent_name());
            }
            if (student.getContact() != null) {
                existingUser.setContact_no(student.getContact());
            }
            userRepository.save(existingUser);
        }

        // Find existing Student
        Optional<Student> s = studentRepository.findById(student_id);
        if (s.isPresent()) {
            Student existingStudent = s.get();

            if (student.getStudent_name() != null) {
                existingStudent.setStudent_name(student.getStudent_name());
            }
            if (student.getContact() != null) {
                existingStudent.setContact(student.getContact());
            }
            if (student.getEmail_id() != null) {
                existingStudent.setEmail_id(student.getEmail_id());
            }
            if (student.getAchievements() != null) {
                existingStudent.setAchievements(student.getAchievements());
            }
            if (student.getCurr_city() != null) {
                existingStudent.setCurr_city(student.getCurr_city());
            }
            if (student.getHome_city() != null) {
                existingStudent.setHome_city(student.getHome_city());
            }
            if (student.getInterest() != null) {
                existingStudent.setInterest(student.getInterest());
            }
            if (student.getSkills() != null) {
                existingStudent.setSkills(student.getSkills());
            }
            if (student.getHobbies() != null) {
                existingStudent.setHobbies(student.getHobbies());
            }
            if (student.getCurr_enroll_no() != null) {
                existingStudent.setCurr_enroll_no(student.getCurr_enroll_no());
            }

            studentRepository.save(existingStudent);
            return existingStudent;
        }

        return null;
    }


    @Override
    public Student addPhoto(Long student_id, MultipartFile file) {
        Optional<Student> student = studentRepository.findById(student_id);
        Student exStudent = student.get();
        try {
            exStudent.setPhoto(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return studentRepository.save(exStudent);
    }

    @Override
    public User changePassword(Long id, String oldPassword, String newPassword) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            return null;
        }

        User existingUser = userOptional.get();

        try {
            if (passwordEncoder.matches(oldPassword, existingUser.getPassword())) {
                existingUser.setPassword(passwordEncoder.encode(newPassword));
                existingUser.setPlainPassword(newPassword);
                userRepository.save(existingUser);
                return existingUser;
            } else {
                return null;
            }
        } catch (Exception e) {
            // Log the exception
            return null;
        }
    }

    @Override
    public byte[] getPhoto(Long studentId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        return (student != null) ? student.getPhoto() : null;
    }

    @Override
    public Map<String, Object> getStudent(Long studentId) {
        Map<String, Object> response = new HashMap<>();
        User user = userRepository.findById(studentId).get();

        String gender = user.getGender();
        Student student = studentRepository.findById(studentId).get();
        response.put("student", student);
        response.put("gender", gender);


        return response;
    }

    @Override
    public User forgotPassword(String email) {
        User existingUser = userRepository.findByEmail(email);
        if (existingUser == null) {
            throw new UserNotFoundException("User not found with email: " + email);
        }

        String generateOTP = generateOTP();
        existingUser.setOtp(generateOTP);
        String body = "<h1 style=\"font-size:55px;margin:20px;text-align:center\">Welcome to <span style=\"color:#1974c3;\">Skill<span style=\"color:#01af36;\">Pilots</span> </h1>" + "<p style=\"text-align:center\"> This is Your One Time Password(OTP) for Password Reset Request - <strong>" + generateOTP + "</strong></p><p style=\"text-align:center\"> Please follow the link" + "<br>Click <a href=''><strong>here</strong></a> to complete your registration." + "<br>You'll be asked to change your password for security reasons.<br></p>" + ProjectConstants.mail_footer;
        userService.sendVerificationEmail(existingUser.getEmail(), "Password Reset Request", body);
        return userService.saveUser(existingUser);
    }

    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        String otp2 = String.valueOf(otp);
        return otp2.substring(0, 4);
    }

    @Override
    public User resetPass(Long studentId, String otp, String newPass) throws Exception {
        Optional<User> userOptional = userRepository.findById(studentId);
        if (!userOptional.isPresent()) {
            return null;
        }
        User existingUser = userOptional.get();
        if (otp.equals(existingUser.getOtp())) {
            existingUser.setPassword(passwordEncoder.encode(newPass));
            userRepository.save(existingUser);
        } else {
            throw new Exception("otp not matching");
        }
        return existingUser;

    }

    @Override
    public List<CollegeInternalReqDto> getClgInternalReqOfStudentWithDept(Long student, Long student_id, Long college_id, Date expiration_date, String status) {
        List<Object[]> resultList = collegeInternalReqRepository.getClgInternalReqOfStudentWithDept(student, student_id, college_id, expiration_date, status);
        List<CollegeInternalReqDto> dtos = new ArrayList<>();
        for (Object[] result : resultList) {
            CollegeInternalReqDto dto = new CollegeInternalReqDto();
            dto.setCompanyName((String) result[0]);
            dto.setUserId((Long) result[1]);
            dto.setRequestMsg((String) result[2]);
            dto.setRejectionMsg((String) result[3]);
            dto.setTechnology((String) result[4]);
            dto.setDuration((int) result[5]);
            dto.setApprovalStatus((String) result[6]);
            dto.setAgainstExtReq((Long) result[7]);
            dto.setForUsers((String) result[8]);
            dto.setCreationDate((Date) result[9]);
            dto.setUpdationDate((Date) result[10]);
            dto.setExpirationDate((Date) result[11]);
            dto.setRequestId((Long) result[12]);
            dto.setTestDataId((Long) result[13]);
            // dto.setTestDataExpirationDate((Date) result[14]);
            dto.setTestDataStatus((String) result[15]);

            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public ExternalRequest getExternalRequestByERid(Long request_id) {
        return externalRepository.findById(request_id).get();
    }

    @Override
    public StudentPast editPast(Long studentId, StudentPast studentPast) {


        StudentPast student = studentPastRepository.findById(studentId).get();
        student.setBoard(studentPast.getBoard());
        student.setCategory(studentPast.getCategory());
        student.setDescription(studentPast.getDescription());
        student.setOrganization(studentPast.getOrganization());
        student.setEnd_date(studentPast.getEnd_date());
        studentPastRepository.save(student);
        return student;
    }

    @Override
    public StudentRequest getStudentReqAgainstAdvNotPresent(Long id) {
        return studentRequestRepository.getStudentReqAgainstAdvNotPresent(id);
    }

    @Override
    public void updateStudentAndPast(Long studentId, StudentUpdateDto dto, Long studpastId) {
        // Update Student
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        // student.setCurr_year(dto.getCurrYear());
        studentRepository.save(student);

        // Update StudentPast
        StudentPast studentPast = studentPastRepository.editStudentPast(studentId, studpastId);

        studentPast.setCategory(dto.getCategory());
        studentPast.setProfile(dto.getProfile());
        studentPast.setOrganization(dto.getOrganization());
        studentPast.setBoard(dto.getBoard());
        studentPast.setDepartment(dto.getDepartment());
        studentPast.setDescription(dto.getDescription());
        studentPast.setEnd_date(dto.getEndDate());
        studentPast.setStart_date(dto.getStartDate());
        studentPast.setUpdation_date(new Date());
        studentPast.setStream(dto.getStream());

        studentPastRepository.save(studentPast);
    }

    @Override
    public StudentPast addStudentPast(Long studentId, StudentUpdateDto dto) {

        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        //  student.setCurr_year(dto.getCurrYear());
        studentRepository.save(student);


        StudentPast studentPast = new StudentPast();
        studentPast.setCategory(dto.getCategory());
        studentPast.setProfile(dto.getProfile());
        studentPast.setOrganization(dto.getOrganization());
        studentPast.setBoard(dto.getBoard());
        studentPast.setDepartment(dto.getDepartment());
        studentPast.setDescription(dto.getDescription());
        studentPast.setEnd_date(dto.getEndDate());
        studentPast.setStudent_id(studentId);
        studentPast.setCreation_date(new Date());
        studentPast.setStart_date(dto.getStartDate());
        studentPast.setStream(dto.getStream());
        return studentPastRepository.save(studentPast);
    }

    @Override
    public List<InternshipDto> getCertificate(Long id) {
        List<InternshipDto> internships = new ArrayList<>();
        List<Object[]> results = internshipRepository.getCertificate(id);
        for (Object[] row : results) {
            InternshipDto dto = new InternshipDto();
            dto.setStudentId((Long) row[0]);  // Corrected casting to Long
            dto.setCompanyId((Long) row[1]);  // Corrected casting to Long
            dto.setCollegeId((Long) row[2]);   //
            dto.setCompanyName((String) row[3]);
            dto.setTechnology((String) row[4]);
            dto.setStudentName((String) row[5]);
            dto.setDuration((int) row[6]);
            dto.setDateStarted((Date) row[7]);
            dto.setDateCompleted((Date) row[8]);
            internships.add(dto);
        }
        return internships;
    }

    @Override
    public Page<StudentListDto> getAllStudentsList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Student> studentPage = studentRepository.findAll(pageable);

        return studentPage.map(student -> {
            StudentListDto dto = new StudentListDto();
            dto.setStudentId(student.getStudent_id());
            dto.setStudentName(student.getStudent_name());
            dto.setEmailId(student.getEmail_id());
            dto.setContact(student.getContact());
            dto.setCollegeId(student.getCollege_id());
            dto.setYear(student.getCurr_year());
            // Check if college_id is not null before fetching the college
            if (student.getCollege_id() != null) {
                College college = collegeRepository.findById(student.getCollege_id()).orElse(null);
                dto.setCollegeName(college != null ? college.getCollege_name() : "Unknown College");
            } else {
                dto.setCollegeName("No College Assigned");
            }

            // Handle department fetching
            if (student.getDept() != null) {
                try {
                    Long deptId = Long.parseLong(student.getDept().toString());
                    Department dep = departmentRepository.findById(deptId).orElse(null);
                    dto.setDepartmentName(dep != null ? dep.getDept_name() : "Unknown Department");
                } catch (NumberFormatException e) {
                    dto.setDepartmentName("Invalid Department ID");
                }
            } else {
                dto.setDepartmentName("No Department Assigned");
            }

            return dto;
        });
    }

    @Override
    public RecruitmentStatus applyDrive(Long studentId, Long recruitId, Long collegeId) {
        RecruitmentStatus status = new RecruitmentStatus();
        status.setStudentId(studentId);
        status.setReqruitmentId(recruitId);
        status.setStatus("Applied");
        status.setCollegeId(collegeId);
        Recruitment recruitment = recruitmentRepository.findById(recruitId).orElseThrow(() -> new RuntimeException("Not found"));

        User user = userRepository.findById(studentId).orElseThrow(() -> new UserNotFoundException("Student NotFound"));

        String candidateMailBody = "<h1 style=\"font-size:55px;margin:20px;text-align:center\">"
                + "<span style=\"color:#1974c3;\">Skill<span style=\"color:#01af36;\">Pilots</span></h1>"
                + "<p style=\"text-align:center\"><strong style=\"color:#414ea4\"> Dear," + user.getName() + "</strong>,</p>"
                + "<p style=\"text-align:center\">Thank you for applying to the position of <strong>" + recruitment.getProfile() + "</strong> at <strong>" + recruitment.getCompanyName() + "</strong>.</p>"
                + "<p style=\"text-align:center\">We have received your application, and the company will review your details shortly. You will be updated on further steps via email.</p>"
                + "<p style=\"text-align:center\">Best of luck with your application process!</p>"
                + "<p style=\"text-align:center\">Feel free to reach out to us if you need any assistance.<br>"
                + "Best regards,<br>The SkillPilots Team</p>"
                + ProjectConstants.mail_footer;


        // Send email to the candidate
        emailService.sendVerificationEmail(user.getEmail(), "Application Received", candidateMailBody);
        String mailBody = "<h1 style=\"font-size:55px;margin:20px;text-align:center\">"
                + "<span style=\"color:#1974c3;\">Skill<span style=\"color:#01af36;\">Pilots</span></h1>"
                + "<p style=\"text-align:center\"><strong style=\"color:#414ea4\">" + recruitment.getCompanyName() + "</strong>,</p>"
                + "<p style=\"text-align:center\">We are pleased to inform you that a new candidate has applied for the position of <strong>" + recruitment.getProfile() + "</strong> as part of your ongoing recruitment drive.</p>"
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
        emailService.sendVerificationEmail(recruitment.getEmail(), "New Candidate Application", mailBody);

        return recruitmentStatusRepo.save(status);
    }

    @Override
    public List<RecruitmentStatus> findAppicanCount() {
        return recruitmentStatusRepo.findAll();
    }

    @Override
    public RecruitmentStatus changeStatus(Long statusId, String status) {
        RecruitmentStatus recruitmentStatus = recruitmentStatusRepo.findById(statusId).orElseThrow(() -> new RuntimeException("Request Not found"));
        recruitmentStatus.setStatus(status);

        User user = userRepository.findById(recruitmentStatus.getStudentId()).orElseThrow(() -> new RuntimeException("Not found0"));

        emailService.sendVerificationEmail(user.getEmail(), "T AND P ", "Update about recruitment");
        return recruitmentStatusRepo.save(recruitmentStatus);
    }

    @Override
    @Transactional
    public Object sendRecruitmentToStudent(List<Long> studentIds, Long recruitmentId, Long collegeId) {

        System.out.println("Student Ids: " + studentIds);
        List<User> students = userRepository.findAllById(studentIds);

        System.out.println("Students: " + students);
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow(() -> new RuntimeException("Not found"));

        String message = recruitment.getCompanyName() + " is hiring for " + recruitment.getPlacementDetails().getJobTitle() + "! Apply now.";

        for (User student : students) {
            if (student.getNotificationToken() != null && !student.getNotificationToken().isEmpty()) {
                firebaseService.sendNotification(student.getNotificationToken(), "T And P..!", message);
            }

            // Create a new RecruitmentStatus for each student
            RecruitmentStatus status = new RecruitmentStatus();
            status.setReqruitmentId(recruitmentId);
            status.setStatus("Applied");
            status.setCollegeId(collegeId);
            status.setStudentId(student.getId());


            recruitmentStatusRepo.save(status);

            emailService.sendVerificationEmail(student.getEmail(), "T And P", message);
        }

        return recruitment;
    }

    @Override
    public List<RecruitmentStatus> updateRound(List<Long> studentIds, Long recruitmentId, String round, String roundTime, String roundDate) {
        List<User> students = userRepository.findAllById(studentIds);
        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow(() -> new RuntimeException("Recruitment not found"));

        List<RecruitmentStatus> updatedStatuses = new ArrayList<>();

        // Batch processing (e.g., in chunks of 50)
        int batchSize = 50;
        for (int i = 0; i < students.size(); i += batchSize) {
            List<User> batch = students.subList(i, Math.min(i + batchSize, students.size()));
            batch.parallelStream().forEach(user -> {
                RecruitmentStatus statuses = recruitmentStatusRepo.findByStudentIdAndReqruitmentId(user.getId(), recruitmentId);
                statuses.setStatus(round);
                String combined = roundDate + " " + roundTime; // e.g., "2025-04-21 22:00"
                String fixedInput = combined.replaceAll("\\s+", " ").trim(); // just to clean any space
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); // 24-hour format
                LocalDateTime roundDateTime = LocalDateTime.parse(fixedInput, formatter);


                RecruitmentStatus status = new RecruitmentStatus();
                statuses.setRoundDateTime(roundDateTime);
                statuses.setRoundDate(roundDate);
                statuses.setRoundTime(roundTime);
                updatedStatuses.add(statuses);
                recruitmentStatusRepo.save(statuses);

                //mail for round update
                String mailBody = "<h1 style=\"font-size:55px;margin:20px;text-align:center\">" + "<span style=\"color:#1974c3;\">Skill<span style=\"color:#01af36;\">Pilots</span></h1>" + "<p style=\"text-align:center\">Dear <strong style=\"color:#414ea4\">" + user.getName() + "</strong>,<br>" + "Congratulations! You are eligible for the next round of the recruitment process.<br>" + "The details for your next round are as follows:</p>" + "<ul style=\"text-align:center; list-style-type:none; padding:0;\">" + "<li><strong>Round:</strong> " + round + "</li>" + "<li><strong>Date & Time:</strong> " + roundDate + " at " + roundTime + "</li>" + "<li><strong>Company:</strong> " + recruitment.getCompanyName() + "</li>" + "</ul>" + "<p style=\"text-align:center\">Please be prepared for the interview and ensure you join on time.<br>" + "If you have any questions, feel free to reach out to us at <strong style=\"color:#414ea4\"><a href=\"http://www.skillpilots.com\" style=\"color:#414ea4; text-decoration:none;\">SkillPilots</a></strong>.<br>" + "Best of luck with your next round!</p>" + ProjectConstants.mail_footer;

                //mail for selection
                String mail = "<h1 style=\"font-size:55px;margin:20px;text-align:center\">" +
                        "<span style=\"color:#1974c3;\">Skill<span style=\"color:#01af36;\">Pilots</span></h1>" +
                        "<p style=\"text-align:center\">Dear <strong style=\"color:#414ea4\">" + user.getName() + "</strong>,<br>" +
                        "We are delighted to inform you that you have been selected for the role of <strong style=\"color:#414ea4\">" + recruitment.getProfile() + "</strong> at <strong style=\"color:#414ea4\">" + recruitment.getCompanyName() + "</strong>!<br>" +
                        "Congratulations on this achievement! We look forward to having you on our team.</p>" +
                        "<p style=\"text-align:center\">If you have any questions, feel free to reach out to us at <strong style=\"color:#414ea4\">" +
                        "<a href=\"http://www.skillpilots.com\" style=\"color:#414ea4; text-decoration:none;\">SkillPilots</a></strong>.<br>" +
                        "Best of luck in your new role!</p>" +
                        ProjectConstants.mail_footer;

                String subject = "Update on Your Application with " + recruitment.getCompanyName();
                // rejection mail
                String reject = "<h1 style=\"font-size:55px;margin:20px;text-align:center\">" +
                        "<span style=\"color:#1974c3;\">Skill<span style=\"color:#01af36;\">Pilots</span></h1>" +
                        "<p style=\"text-align:center\">Dear <strong style=\"color:#414ea4\">" + user.getName() + "</strong>,<br>" +
                        "Thank you for your interest in the <strong style=\"color:#414ea4\">" + recruitment.getProfile() + "</strong> position at <strong style=\"color:#414ea4\">" + recruitment.getCompanyName() + "</strong>.<br>" +
                        "We appreciate the time and effort you put into your application.</p>" +
                        "<p style=\"text-align:center\">After careful review, we have decided to move forward with other candidates for this role. We encourage you to apply for future opportunities with us, as we were impressed with your qualifications and would be glad to consider you for roles that may better align with our current needs.</p>" +
                        "<p style=\"text-align:center\">If you have any questions, feel free to reach out to us at <strong style=\"color:#414ea4\">" +
                        "<a href=\"http://www.skillpilots.com\" style=\"color:#414ea4; text-decoration:none;\">SkillPilots</a></strong>.<br>" +
                        "We wish you all the best in your career journey!</p>" +
                        ProjectConstants.mail_footer;

                if (round.equals("SELECTED")) {
                    emailService.sendVerificationEmail(user.getEmail(), subject, mail);

                } else if (round.equals("REJECTED")) {
                    emailService.sendVerificationEmail(user.getEmail(), subject, reject);

                } else {
                    emailService.sendVerificationEmail(user.getEmail(), "Next Round Information", mailBody);

                }

                // Send email asynchronously
            });
        }

        return updatedStatuses;
    }

    @Override
    public List<RecruitmentWithStatusDTO> getRecruitmentsWithStatus(Long collegeId, Long studentId) {
        List<Recruitment> recruitments = recruitmentRepository.findByStatusAndCollegeId("A", collegeId);

        List<RecruitmentWithStatusDTO> recruitmentWithStatusList = new ArrayList<>();

        for (Recruitment recruitment : recruitments) {
            RecruitmentWithStatusDTO dto = new RecruitmentWithStatusDTO();
            dto.setPlacementId(recruitment.getPlacementId());
            dto.setCompanyName(recruitment.getCompanyName());
            dto.setProfile(recruitment.getProfile());
            dto.setCtc(recruitment.getCtc());
            dto.setDepartmentIds(recruitment.getDepartmentIds());

            RecruitmentStatus status = recruitmentStatusRepo.findByStudentIdAndReqruitmentId(studentId, recruitment.getPlacementId());

            if (status != null) {
                dto.setStatus(status.getStatus());
                dto.setApplyDate(status.getApplyDate());
                dto.setRound(status.getRound());
            } else {
                dto.setStatus("Not Applied");
            }

            recruitmentWithStatusList.add(dto);
        }

        return recruitmentWithStatusList;
    }


    @Override
    public List<RecruitmentStatus> updateCompanyRound(List<Long> studentIds, Long driveId, String round, String roundTime, String roundDate, List<Long> id, String meetLink) {
        CompanyDrive companyDrive = companyDriveRepository.findById(driveId).orElseThrow(() -> new RuntimeException("Drive Not found"));
        List<User> students = userRepository.findAllById(studentIds);
        List<RecruitmentStatus> updatedStatuses = new ArrayList<>();

        int batchSize = 50;
        for (int i = 0; i < students.size(); i += batchSize) {
            List<User> batch = students.subList(i, Math.min(i + batchSize, students.size()));
            batch.parallelStream().forEach(user -> {
                RecruitmentStatus statuses = recruitmentStatusRepo.findByStudentIdAndDriveId(user.getId(), driveId);
                statuses.setStatus(round);
                String combined = roundDate + " " + roundTime; // e.g., "2025-04-21 22:00"
                String fixedInput = combined.replaceAll("\\s+", " ").trim(); // just to clean any space
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); // 24-hour format
                LocalDateTime roundDateTime = LocalDateTime.parse(fixedInput, formatter);
                statuses.setRoundDateTime(roundDateTime);

                statuses.setRoundDate(roundDate);
                statuses.setRoundTime(roundTime);
                recruitmentStatusRepo.save(statuses);
                updatedStatuses.add(statuses);
                //mail for round update
                StringBuilder mailBody = new StringBuilder("<h1 style=\"font-size:55px;margin:20px;text-align:center\">" +
                        "<span style=\"color:#1974c3;\">Skill<span style=\"color:#01af36;\">Pilots</span></h1>" +
                        "<p style=\"text-align:center\">Dear <strong style=\"color:#414ea4\">" + user.getName() + "</strong>,<br>" +
                        "Congratulations! You are eligible for the next round of the recruitment process.<br>" +
                        "The details for your next round are as follows:</p>" +
                        "<ul style=\"text-align:center; list-style-type:none; padding:0;\">" +
                        "<li><strong>Round:</strong> " + round + "</li>" +
                        "<li><strong>Date & Time:</strong> " + roundDate + " at " + roundTime + "</li>" +
                        "<li><strong>Company:</strong> " + companyDrive.getCompanyName() + "</li>" +
                        "</ul>");

                if (meetLink != null && !meetLink.isEmpty()) {
                    mailBody.append("<p style=\"text-align:center\">Please login at <a href=\"http://www.skillpilots.com\" style=\"color:#414ea4; text-decoration:none;\">SkillPilots</a> to join the meeting with code: <a href=\"")

                            .append("\" style=\"color:#414ea4;\">")
                            .append(meetLink.substring(meetLink.lastIndexOf("si/") + 3))
                            .append("</a></p>");

                }

                mailBody.append("<p style=\"text-align:center\">Please be prepared for the interview and ensure you join on time.<br>" + "If you have any questions, feel free to reach out to us at <strong style=\"color:#414ea4\"><a href=\"http://www.skillpilots.com\" style=\"color:#414ea4; text-decoration:none;\">SkillPilots</a></strong>.<br>" + "Best of luck with your next round!</p>" + ProjectConstants.mail_footer);

                //mail for selection
                String mail = "<h1 style=\"font-size:55px;margin:20px;text-align:center\">" +
                        "<span style=\"color:#1974c3;\">Skill<span style=\"color:#01af36;\">Pilots</span></h1>" +
                        "<p style=\"text-align:center\">Dear <strong style=\"color:#414ea4\">" + user.getName() + "</strong>,<br>" +
                        "We are delighted to inform you that you have been selected for the role of <strong style=\"color:#414ea4\">" + companyDrive.getJobRole() + "</strong> at <strong style=\"color:#414ea4\">" + companyDrive.getCompanyName() + "</strong>!<br>" +
                        "Congratulations on this achievement! We look forward to having you on our team.</p>" +
                        "<p style=\"text-align:center\">If you have any questions, feel free to reach out to us at <strong style=\"color:#414ea4\">" +
                        "<a href=\"http://www.skillpilots.com\" style=\"color:#414ea4; text-decoration:none;\">SkillPilots</a></strong>.<br>" +
                        "Best of luck in your new role!</p>" +
                        ProjectConstants.mail_footer;

                String subject = "Update on Your Application with " + companyDrive.getCompanyName();
                // rejection mail
                String reject = "<h1 style=\"font-size:55px;margin:20px;text-align:center\">" +
                        "<span style=\"color:#1974c3;\">Skill<span style=\"color:#01af36;\">Pilots</span></h1>" +
                        "<p style=\"text-align:center\">Dear <strong style=\"color:#414ea4\">" + user.getName() + "</strong>,<br>" +
                        "Thank you for your interest in the <strong style=\"color:#414ea4\">" + companyDrive.getJobRole() + "</strong> position at <strong style=\"color:#414ea4\">" + companyDrive.getCompanyName() + "</strong>.<br>" +
                        "We appreciate the time and effort you put into your application.</p>" +
                        "<p style=\"text-align:center\">After careful review, we have decided to move forward with other candidates for this role. We encourage you to apply for future opportunities with us, as we were impressed with your qualifications and would be glad to consider you for roles that may better align with our current needs.</p>" +
                        "<p style=\"text-align:center\">If you have any questions, feel free to reach out to us at <strong style=\"color:#414ea4\">" +
                        "<a href=\"http://www.skillpilots.com\" style=\"color:#414ea4; text-decoration:none;\">SkillPilots</a></strong>.<br>" +
                        "We wish you all the best in your career journey!</p>" +
                        ProjectConstants.mail_footer;

                // find manager
                List<User> manager = userRepository.findAllById(id);


                if (round.equals("SELECTED")) {
                    emailService.sendVerificationEmail(user.getEmail(), subject, mail);

                } else if (round.equals("REJECTED")) {
                    emailService.sendVerificationEmail(user.getEmail(), subject, reject);

                } else {

                    emailService.sendVerificationEmail(user.getEmail(), "Next Round Information", mailBody.toString());
                    for (User u : manager) {
                        String mailBody2 = "<h1 style=\"font-size:55px;margin:20px;text-align:center\">" +
                                "<span style=\"color:#1974c3;\">Skill<span style=\"color:#01af36;\">Pilots</span></h1>" +
                                "<p style=\"text-align:center\">Dear <strong style=\"color:#414ea4\">" + u.getName() + "</strong>,<br>" +
                                "You have been assigned to conduct an interview in the next round of the recruitment process.<br>" +
                                "Here are the details for the upcoming interview:</p>" +
                                "<ul style=\"text-align:center; list-style-type:none; padding:0;\">" +
                                "<li><strong>Candidate Name:</strong> " + user.getName() + "</li>" +
                                "<li><strong>Round:</strong> " + round + "</li>" +
                                "<li><strong>Date & Time:</strong> " + roundDate + " at " + roundTime + "</li>" +
                                "<li><strong>Position:</strong> " + companyDrive.getJobRole() + "</li>" +
                                "<li><strong>Company:</strong> " + companyDrive.getCompanyName() + "</li>" +
                                "</ul>";
                        if (meetLink != null && !meetLink.isEmpty()) {
                            mailBody2 += "<p style=\"text-align:center\">Please Log In join the meeting using the following Code: <a href=\"" + meetLink + "\" style=\"color:#414ea4;\">" + meetLink.substring(meetLink.lastIndexOf("si/") + 3) + "</a></p>";
                        }

                        mailBody2 +=
                                "If you need additional information, feel free to reach out to us at <strong style=\"color:#414ea4\"><a href=\"http://www.skillpilots.com\" style=\"color:#414ea4; text-decoration:none;\">SkillPilots</a></strong>.<br>" +
                                        "Thank you for your involvement in this recruitment process!</p>" +
                                        ProjectConstants.mail_footer;
                        emailService.sendVerificationEmail(u.getEmail(), "Regarding Interview !", mailBody2);
                    }

                }
            });
        }

        return updatedStatuses;
    }

    @Override
    public Page<StudentListDto> searchStudentsByName(String name, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Student> studentPage = studentRepository.getFilterStudent(name, pageable);

        return studentPage.map(student -> {
            StudentListDto dto = new StudentListDto();
            dto.setStudentId(student.getStudent_id());
            dto.setStudentName(student.getStudent_name());
            dto.setEmailId(student.getEmail_id());
            dto.setContact(student.getContact());
            dto.setCollegeId(student.getCollege_id());

            if (student.getCollege_id() != null) {
                College college = collegeRepository.findById(student.getCollege_id()).orElse(null);
                dto.setCollegeName(college != null ? college.getCollege_name() : "Unknown College");
            } else {
                dto.setCollegeName("No College Assigned");
            }

            // Handle department fetching
            if (student.getDept() != null) {
                try {
                    Long deptId = Long.parseLong(student.getDept().toString());
                    Department dep = departmentRepository.findById(deptId).orElse(null);
                    dto.setDepartmentName(dep != null ? dep.getDept_name() : "Unknown Department");
                } catch (NumberFormatException e) {
                    dto.setDepartmentName("Invalid Department ID");
                }
            } else {
                dto.setDepartmentName("No Department Assigned");
            }

            return dto;
        });
    }


    @Override
    public List<StudentListDto> getStudentList() {
        List<Student> students = studentRepository.getAllStudentsList();
        List<StudentListDto> studentListDtos = new ArrayList<>();
        for (Student student : students) {
            StudentListDto dto = new StudentListDto();
            dto.setStudentId(student.getStudent_id());
            dto.setStudentName(student.getStudent_name());
            dto.setEmailId(student.getEmail_id());
            dto.setContact(student.getContact());
            dto.setCollegeId(student.getCollege_id());
            if (student.getCollege_id() != null) {
                College college = collegeRepository.findById(student.getCollege_id()).orElse(null);
                if (college != null) {
                    dto.setCollegeName(college.getCollege_name());
                } else {
                    dto.setCollegeName("Unknown College");
                }
            } else {
                dto.setCollegeName("No College Assigned");
            }
            // Handle department fetching
            if (student.getDept() != null) {
                try {
                    // Convert dept (assumed to be String) to Long
                    Long deptId = Long.parseLong(student.getDept().toString());
                    Department dep = departmentRepository.findById(deptId).orElse(null);
                    if (dep != null) {
                        dto.setDepartmentName(dep.getDept_name());
                    } else {
                        dto.setDepartmentName("Unknown Department");
                    }
                } catch (NumberFormatException e) {
                    dto.setDepartmentName("Invalid Department ID");
                }
            } else {
                dto.setDepartmentName("No Department Assigned");
            }
            studentListDtos.add(dto);
        }
        System.out.println("Fetched student list: " + studentListDtos);
        return studentListDtos;
    }

    @Override
    public Page<StudentListDto> getByDeptAndColege(int page, int size, Long collegeId, Long deptId) {
        Pageable pageable = PageRequest.of(page, size);

        // Fetch raw data
        Page<Object[]> rawData = userRepository.getCollegeAndDeptData(collegeId, deptId, "0", pageable);

        // Map to DTO
        return rawData.map(record -> {
            StudentListDto dto = new StudentListDto();

            // Set fields based on query result positions
            dto.setStudentName((String) record[0]);
            dto.setEmailId((String) record[1]);
            //   dto.setCreationDate((record[2] != null) ? (LocalDate) record[2] : null);
            dto.setYear((record[3] != null) ? (String) record[3] : null);
            dto.setCollegeName((record[4] != null) ? (String) record[4] : "Unknown College");

            Long dept = (Long) record[5];
            Department dep = departmentRepository.findById(dept).orElse(null);
            if (dep != null) {
                dto.setDepartmentName(dep.getDept_name()); // Set department name if found
            } else {
                dto.setDepartmentName("Unknown Department"); // Set a default value if department not found
            }
            return dto;
        });
    }

    @Override
    public Page<StudentListDto> getInternalStudent(int page, int size, Long collegeId) {
        Pageable pageable = PageRequest.of(page, size);
        if (!(collegeId == 0)) {
            Page<Object[]> rawData = userRepository.getInternalStudent(collegeId, "0", pageable);
            return rawData.map(record -> {
                StudentListDto dto = new StudentListDto();

                dto.setStudentName((String) record[0]);
                dto.setEmailId((String) record[1]);
                //   dto.setCreationDate((record[2] != null) ? (LocalDate) record[2] : null);
                dto.setYear((record[3] != null) ? (String) record[3] : null);
                dto.setCollegeName((record[4] != null) ? (String) record[4] : "Unknown College");

                Long dept = (Long) record[5];
                dto.setDeptId(dept);
                Long colId = Long.valueOf((Integer) record[6]);
                dto.setCollegeId(colId);
                Department dep = departmentRepository.findById(dept).orElse(null);
                if (dep != null) {
                    dto.setDepartmentName(dep.getDept_name()); // Set department name if found
                } else {
                    dto.setDepartmentName("Unknown Department"); // Set a default value if department not found
                }
                return dto;
            });
        } else {
            Pageable pageable1 = PageRequest.of(page, size);
            Page<Object[]> exteStudents = userRepository.getExteStudents("10", pageable1);
            return exteStudents.map(record -> {
                StudentListDto dto = new StudentListDto();

                // Set fields based on query result positions
                dto.setStudentName((String) record[0]);
                dto.setEmailId((String) record[1]);
                //   dto.setCreationDate((record[2] != null) ? (LocalDate) record[2] : null);
                dto.setYear((record[3] != null) ? (String) record[3] : null);
                // dto.setCollegeName((record[4] != null) ? (String) record[4] : "Unknown College");

                Long dept = (Long) record[4];
                if (dept != null) {
                    Department dep = departmentRepository.findById(dept).orElse(null);
                    if (dep != null) {
                        dto.setDepartmentName(dep.getDept_name()); // Set department name if found
                    } else {
                        dto.setDepartmentName("Unknown Department"); // Set a default value if department not found
                    }
                } else {
                    dto.setDepartmentName("Unknown");
                }
                return dto;
            });

        }
    }

    @Override
    public List<StudentListDto> getStudentsWithoutFilter(Long collegeId) {
        if (collegeId != null && collegeId != 0) {
            // Fetch internal students based on collegeId
            List<Object[]> rawData = userRepository.getInternalStudentNoFilter(collegeId, "0");
            return rawData.stream().map(record -> {
                StudentListDto dto = new StudentListDto();

                // Map raw query results to DTO
                dto.setStudentName((String) record[0]);
                dto.setEmailId((String) record[1]);
                dto.setYear(record[3] != null ? (String) record[3] : null);
                dto.setCollegeName(record[4] != null ? (String) record[4] : "Unknown College");

                Long deptId = (Long) record[5];
                dto.setDeptId(deptId);

                Long collegeIdFromRecord = Long.valueOf((Integer) record[6]);
                dto.setCollegeId(collegeIdFromRecord);

                Department department = departmentRepository.findById(deptId).orElse(null);
                dto.setDepartmentName(department != null ? department.getDept_name() : "Unknown Department");

                return dto;
            }).collect(Collectors.toList());
        } else {
            // Fetch external students when collegeId is not provided
            List<Object[]> externalStudents = userRepository.getExteStudentsWithoutFilter("10");
            return externalStudents.stream().map(record -> {
                StudentListDto dto = new StudentListDto();

                // Map raw query results to DTO
                dto.setStudentName((String) record[0]);
                dto.setEmailId((String) record[1]);
                dto.setYear(record[3] != null ? (String) record[3] : null);

                Long deptId = (Long) record[4];
                dto.setDeptId(deptId);

                Department department = deptId != null ? departmentRepository.findById(deptId).orElse(null) : null;
                dto.setDepartmentName(department != null ? department.getDept_name() : "Unknown Department");

                return dto;
            }).collect(Collectors.toList());
        }
    }

    @Override
    public List<StudentListDto> getDeptAndCollegeWithoutFilter(Long collegeId, Long deptId, String year) {
        // Fetch raw data from the repository
        List<Object[]> rawData = userRepository.getCollegeAndDeptWithoutFilter(collegeId, deptId,"0",year);

        // Debugging: Check raw data
        System.out.println("Raw Query Results:");
        rawData.forEach(record -> System.out.println(Arrays.toString(record)));

        // Map to DTO
        return rawData.stream()
                .filter(record -> {
                    // Filter out unexpected results for additional safety
                    Long recordDeptId = record[5] != null ? (Long) record[5] : null;
                    return deptId == null || deptId.equals(recordDeptId);
                })
                .map(record -> {
                    StudentListDto dto = new StudentListDto();

                    // Map fields based on query result positions
                    dto.setStudentName((String) record[0]);
                    dto.setEmailId((String) record[1]);
                    dto.setYear(record[3] != null ? (String) record[3] : null);
                    dto.setCollegeName(record[4] != null ? (String) record[4] : "Unknown College");

                    // Set department name
                    String departmentName = record[6] != null ? (String) record[6] : "Unknown Department";
                    dto.setDepartmentName(departmentName);

                    return dto;
                })
                .collect(Collectors.toList());
    }

}
//    @Override
//    public Page<StudentListDto> getCollegeAndDeptDetails(int page, int size, Long collegeId, Long deptId, String role) {
//        return null;
//    }








	

	

	
	

