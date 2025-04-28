package com.skilladmin.serviceImpl;

import com.skilladmin.config.JwtUtils;
import com.skilladmin.dto.BatchAndFees;
import com.skilladmin.dto.LoginRequest;
import com.skilladmin.dto.TraineeStudentRegistration;
import com.skilladmin.dto.TraineeUpdateDto;
import com.skilladmin.exception.UserNotFoundException;
import com.skilladmin.model.Batch;
import com.skilladmin.model.Student;
import com.skilladmin.model.TrainingUsers;
import com.skilladmin.model.User;
import com.skilladmin.repository.BatchRepository;
import com.skilladmin.repository.StudentRepository;
import com.skilladmin.repository.TrainingUserRepository;
import com.skilladmin.repository.TutorRepository;
import com.skilladmin.repository.UserRepository;
import com.skilladmin.service.BatchService;
import com.skilladmin.service.PaidInstallmentsService;
import com.skilladmin.service.TrainingUserService;
import com.skilladmin.service.UserService;
import com.skilladmin.util.ProjectConstants;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class TrainingUserSerImpl implements TrainingUserService, UserDetailsService {

    public  static final String UPLOAD_DIR="src/main/resources/static/images/";


    private  final TrainingUserRepository  trainingUserRepository;

    private final PasswordEncoder passwordEncoder;

    private  final UserRepository userRepository;

    private  final StudentRepository studentRepository;
    private  final TutorRepository tutorRepository;
    private  final JwtUtils jwtUtils;

    @Autowired
    private BatchRepository batchRepository;
    
    @Autowired
    private PaidInstallmentsService installmentsService;
    
    private final UserService userService ;
    public TrainingUserSerImpl(TrainingUserRepository trainingUserRepository, PasswordEncoder passwordEncoder, UserRepository userRepository, StudentRepository studentRepository, TutorRepository tutorRepository, JwtUtils jwtUtils, UserService userService) {
        this.trainingUserRepository = trainingUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.tutorRepository = tutorRepository;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Transactional
        @Override
        public TrainingUsers craeteTraingStudents(TraineeStudentRegistration registration) {

        User existingUser = userRepository.findByEmail(registration.getEmail());
        if (existingUser != null) {
            throw new RuntimeException("Email is already present: " + registration.getEmail());
        }
        User  user= new User();
        Student student= new Student();

        // Create and populate the TrainingUsers object
        TrainingUsers trainingUsers = new TrainingUsers();
        trainingUsers.setEmail(registration.getEmail());  // Set the email
        trainingUsers.setMobNo(registration.getMobNo());
        trainingUsers.setRole(ProjectConstants.trainee);
        trainingUsers.setDob(registration.getDob());
        trainingUsers.setAddress(registration.getAddress());
        trainingUsers.setGender(registration.getGender());
        trainingUsers.setName(registration.getName());
        trainingUsers.setDesignation(registration.getDesignation());
        trainingUsers.setStatus(true);

        // Set the password and role for the User object
        user.setPassword(passwordEncoder.encode(registration.getMobNo().toString()));
        user.setRole(ProjectConstants.trainee);
        user.setType((short) 2);
        user.setGender(registration.getGender());
        user.setEmail(registration.getEmail());
        user.setUsername(registration.getName());
        user.setContact_no(registration.getMobNo());
        // Save the User object
        userRepository.save(user);
        System.out.println("ekjhdhwudfyiuy"+user.getUsername());

        student.setStudent_id(user.getId());
        student.setContact(registration.getMobNo());
        student.setStudent_name(registration.getName());
        student.setEmail_id(registration.getEmail());
        student.setCreation_date(new Date());

        studentRepository.save(student);
        // Correctly set the username for TrainingUsers
        trainingUsers.setName(registration.getName());

        // Set the user ID in TrainingUsers and save it
        trainingUsers.setStudentId(user.getId());
        trainingUserRepository.save(trainingUsers);

        String body = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body {font-family: Arial, sans-serif; margin: 0; padding: 0;}" +
                ".container {padding: 20px;}" +
                ".header {background-color: #f4f4f4; padding: 10px 20px; text-align: center;}" +
                ".content {padding: 20px;}" +
                ".footer {background-color: #f4f4f4; padding: 10px 20px; text-align: center;}" +
                ".info {font-weight: bold;}" +
                ".app-download {margin-top: 20px; text-align: center;}" +
                ".playstore-icon {width: 150px; height: auto;}" + // Adjust the size of the icon
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h2>Welcome to Cluematrix Technology!</h2>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Dear " + registration.getName() + ",</p>" +
                "<p>Thank you for registering with us. Below are your login details:</p>" +
                "<p><span class='info'>Email:</span> " + registration.getEmail() + "</p>" +
                "<p><span class='info'>Password:</span> " + registration.getMobNo() + "</p>" +
                "<p>Please make sure to change your password after your first login.</p>" +
                "<p>If you have any questions, feel free to contact our support team.</p>" +
                "<div class='app-download'>" +
                "<p>Download our app from the Play Store:</p>" +
                "<a href='https://play.google.com/store/apps/details?id=com.cluematrixtechnologies.skillpilots&pcampaignid=web_share <img class='playstore-icon' src='https://upload.wikimedia.org/wikipedia/commons/7/78/Google_Play_Store_badge_EN.svg' alt='Play Store Download'/>" +
                "</a>`" +
                "</div>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>&copy; <span id='year'></span> Cluematrix Technology! All rights reserved.</p>" +
                "</div>" +
                "</div>" +
                "<script>" +
                "document.getElementById('year').textContent = new Date().getFullYear();" +
                "</script>" +
                "</body>" +
                "</html>";



        System.out.println("ekjhdhwudfyiuy"+user);

        userService.sendVerificationEmail(registration.getEmail(), "Password", body);
            return trainingUsers;
        }

    @Override
    public LoginRequest login(LoginRequest loginRequest) {

        try {


            TrainingUsers trainingUsers = trainingUserRepository.findByEmail(loginRequest.getEmail());
            if (trainingUsers == null) {
                throw new UserNotFoundException("User Not found " + loginRequest.getEmail());
            }
            boolean passwordMatches = passwordEncoder.matches(loginRequest.getPassword(), trainingUsers.getPassword());

            if (!passwordMatches) {
                throw new RuntimeException("Invalid password");
            }
            String token = jwtUtils.generateToken(trainingUsers);

            loginRequest.setMsg("User Log In");
            loginRequest.setStatus("200");
            loginRequest.setTrainingUsers(trainingUsers);
            loginRequest.setToken(token);

            return loginRequest;
        }catch(UserNotFoundException e){
            loginRequest.setMsg("User Not Found ");
            loginRequest.setStatus("400");
            return loginRequest;

        } catch (RuntimeException e) {
            loginRequest.setMsg("Inavlid user or password ");
            loginRequest.setStatus("400");
            loginRequest.setMsg(e.getMessage());
            return loginRequest;

        }
    }

    @Override
    public List<TrainingUsers> getStudensBatchVice(Long batchId) {
        return trainingUserRepository.findByBatchIdAndRoleAndStatus(batchId,"isTrainee",true);
    }

    @Override
    public TrainingUsers addManager(TrainingUsers trainingUsers) {
        TrainingUsers trainingUsers1 = trainingUserRepository.findByEmail(trainingUsers.getEmail());
        if (trainingUsers1 != null) {
            throw new RuntimeException("Email is already present: " + trainingUsers.getEmail());
        }
        trainingUsers.setRole("Manager");
        trainingUsers.setPassword(passwordEncoder.encode(trainingUsers.getMobNo().toString()));
        String body = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<style>" +
                "body {font-family: Arial, sans-serif; margin: 0; padding: 0;}" +
                ".container {padding: 20px;}" +
                ".header {background-color: #f4f4f4; padding: 10px 20px; text-align: center;}" +
                ".content {padding: 20px;}" +
                ".footer {background-color: #f4f4f4; padding: 10px 20px; text-align: center;}" +
                ".info {font-weight: bold;}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h2>Welcome to Cluematrix Technology!</h2>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Dear " + trainingUsers.getName() + ",</p>" +
                "<p>Thank you for registering with us. Below are your login details:</p>" +
                "<p><span class='info'>Email:</span> " + trainingUsers.getEmail() + "</p>" +
                "<p><span class='info'>Password:</span> " + trainingUsers.getMobNo() + "</p>" +
                "<p>Please make sure to change your password after your first login.</p>" +
                "<p>If you have any questions, feel free to contact our support team.</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>&copy; <span id='year'></span> Cluematrix Technology!. All rights reserved.</p>" +
                "</div>" +
                "</div>" +
                "<script>" +
                "document.getElementById('year').textContent = new Date().getFullYear();" +
                "</script>" +
                "</body>" +
                "</html>";


        trainingUserRepository.save(trainingUsers);
        userService.sendVerificationEmail(trainingUsers.getEmail(), "Password", body);

        return trainingUsers;
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
    public List<Object[]> getAllTraiStudents() {
        return trainingUserRepository.findByRoles(ProjectConstants.trainee) ;
    }

    @Override
    public TrainingUsers getTrainingUserById(Long id) {
        return trainingUserRepository.findById(id).orElseThrow(()->new
                RuntimeException("User not found"));
    }

    @Override
    public TrainingUsers uploadPhotoManager(Long id, MultipartFile file) throws IOException {
        TrainingUsers trainingUsers=null;
        if (!file.isEmpty()) {
            Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);
            String fileName = file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            // Save file to the server
            file.transferTo(filePath.toFile());
            // Construct the full URL
            String imageUrl = "http://localhost:8081/newskill/images/" + fileName;
            //     String imageUrl = "https://shubhamkohad.site/images/" + fileName;
            // Save image path as URL
             trainingUsers=   trainingUserRepository.findById(id)
                    .orElseThrow(()-> new RuntimeException("Manager Not Found:"+id));
            trainingUsers.setImagePath(imageUrl);

        }

        // Save the product with the full image URL

        assert trainingUsers != null;
        return    trainingUserRepository.save(trainingUsers);
    }

    @Override
    public List<TrainingUsers> getAllUser() {
        return trainingUserRepository.findByStatus(true);
    }

    @Override
    public TrainingUsers updateStudent(Long studentId, TraineeUpdateDto trainingUsers) {
        TrainingUsers exUsers = trainingUserRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (trainingUsers != null) {
            // Update only non-null fields
            if (trainingUsers.getName() != null) {
                exUsers.setName(trainingUsers.getName());
            }
            if (trainingUsers.getGender() != null) {
                exUsers.setGender(trainingUsers.getGender());
            }
            if (trainingUsers.getAddress() != null) {
                exUsers.setAddress(trainingUsers.getAddress());
            }
            if (trainingUsers.getDob() != null) {
                exUsers.setDob(trainingUsers.getDob());
            }
            if (trainingUsers.getMobNo() != null) {
                exUsers.setMobNo(trainingUsers.getMobNo());
            }if(trainingUsers.getDesignation()!=null){
                exUsers.setDesignation(trainingUsers.getDesignation());
            }


            // Save updated TrainingUsers entity
            trainingUserRepository.save(exUsers);

            // Update the User entity
            User user = userRepository.findById(exUsers.getStudentId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            if (trainingUsers.getName() != null) {
                user.setUsername(trainingUsers.getName());
            }
            if (trainingUsers.getGender() != null) {
                user.setGender(trainingUsers.getGender());
            }
            if (trainingUsers.getMobNo() != null) {
                user.setContact_no(trainingUsers.getMobNo());
            }
            userRepository.save(user);

            // Update the Student entity
            Student student = studentRepository.findById(exUsers.getStudentId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            if (trainingUsers.getName() != null) {
                student.setStudent_name(trainingUsers.getName());
            }
            if (trainingUsers.getMobNo() != null) {
                student.setContact(trainingUsers.getMobNo());
            }
            if (trainingUsers.getSkills()!=null){
                student.setSkills(trainingUsers.getSkills());
            }
            if(trainingUsers.getInterest()!=null){
                student.setInterest(trainingUsers.getInterest());
            }
            studentRepository.save(student);
        }

        return exUsers;
    }

    @Override
    public void deleteStudent(Long studentId) {
        // Find the TrainingUsers entity by studentId
        TrainingUsers exUsers = trainingUserRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userRepository.existsById(exUsers.getStudentId())) {
            userRepository.deleteById(exUsers.getStudentId());
        }

        if (studentRepository.existsById(exUsers.getStudentId())) {
            studentRepository.deleteById(exUsers.getStudentId());
        }
        trainingUserRepository.delete(exUsers);
    }

    @Override
    public TrainingUsers deactivateUser(Long userId) {
        TrainingUsers exUsers = trainingUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        exUsers.setStatus(!exUsers.isStatus());

        trainingUserRepository.save(exUsers);
        return exUsers;
    }


    @Override
    public TrainingUsers activateUser(Long userId) {
        TrainingUsers exUsers = trainingUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        exUsers.setStatus(true);
        trainingUserRepository.save(exUsers);
        return exUsers;
    }

    @Override
    public List<TrainingUsers> findByStatus(boolean status) {
        return trainingUserRepository.findByStatus(false);
    }

    @Override
    public Optional<TrainingUsers> getTrainId(Long id) {
        return trainingUserRepository.findById(id);
    }

    @Override
    public Object[] getStudentsWithBatch(Long studentId) {
        return trainingUserRepository.getStudentsWithBatch(studentId);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TrainingUsers trainingUser = trainingUserRepository.findByEmail(username);
        if (trainingUser == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        return new org.springframework.security.core.userdetails.User(trainingUser.getEmail(), trainingUser.getPassword(), getAuthorities(trainingUser));
    }
    private Collection<? extends GrantedAuthority> getAuthorities(TrainingUsers trainingUser) {
        // Convert user roles to authorities
        return Collections.singletonList(new SimpleGrantedAuthority(trainingUser.getRole()));
    }

	@Override
	public Optional<BatchAndFees> getbatchAndFees(Long id) {
		
	
		
	Optional<TrainingUsers> tuser = 	trainingUserRepository.findById(id);
	
	if (tuser.isPresent()) {
	    Long bid = tuser.get().getBatchId();
	    Long fees = tuser.get().getBatchfees();
	    
	    Optional<Batch> batch = batchRepository.findById(bid);
	    

	    Double tpaid = installmentsService.totalPaidById(id);
	    
	
	    
	    BatchAndFees batchAndFees = new BatchAndFees();
		batchAndFees.setBatch(batch.get().getBatchName());
		batchAndFees.setBatchfees(fees);
		batchAndFees.setTpaid(tpaid);
		return Optional.of(batchAndFees);
	  
	}

	
		
		// TODO Auto-generated method stub
		return Optional.empty();
	}
}
