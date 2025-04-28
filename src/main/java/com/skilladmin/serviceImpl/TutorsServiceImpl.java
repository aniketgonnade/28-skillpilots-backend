package com.skilladmin.serviceImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.skilladmin.model.Batch;
import com.skilladmin.model.TrainingCourses;
import com.skilladmin.model.TrainingUsers;
import com.skilladmin.model.Tutors;
import com.skilladmin.repository.BatchRepository;
import com.skilladmin.repository.TrainingCourseRepository;
import com.skilladmin.repository.TrainingUserRepository;
import com.skilladmin.repository.TutorRepository;
import com.skilladmin.service.TutorsService;
import com.skilladmin.service.UserService;

@Service
public class TutorsServiceImpl implements TutorsService {

    public static final String UPLOAD_DIR = "app/images";


    private final TutorRepository tutorRepository;

    private final BatchRepository batchRepository;

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;


    private final TrainingUserRepository trainingUserRepository;

//    private final TrainingCourseRepository trainingCourseRepository;
//    private  final  TrainingCourses trainingCourses;

//    @Autowired
//    private FileUploadHelper fileUploadHelper;

    @Autowired
    private TrainingCourseRepository trainingCourseRepository;

    public TutorsServiceImpl(TutorRepository tutorRepository, BatchRepository batchRepository, PasswordEncoder passwordEncoder, UserService userService, TrainingUserRepository trainingUserRepository) {
        this.tutorRepository = tutorRepository;
        this.batchRepository = batchRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.trainingUserRepository = trainingUserRepository;
//       this.trainingCourses = trainingCourses;
    }


    @Override
    public Tutors create(List<Long> batchIds, Tutors tutors,String address) {

        Tutors tutors1 = tutorRepository.findByEmail(tutors.getEmail());
        if (tutors1 != null) {
            throw new RuntimeException("Email is already present: " + tutors.getEmail());

        }
        TrainingUsers trainingUsers = new TrainingUsers();

        tutors.setPassword(passwordEncoder.encode(tutors.getMobNo().toString()));

        List<Batch> batch = batchRepository.findAllById(batchIds);
        for (Batch batch1 : batch) {
            batch1.setTutors(tutors);
            trainingUsers.setBatchId(batch1.getBatchId());
            tutors.getBatches().add(batch1);

        }
        trainingUsers.setEmail(tutors.getEmail());
        trainingUsers.setMobNo(tutors.getMobNo());
        trainingUsers.setPassword(passwordEncoder.encode(tutors.getMobNo().toString()));
        trainingUsers.setName(tutors.getName());
        trainingUsers.setRole("TUTOR");
        trainingUsers.setGender(tutors.getGender());
        trainingUsers.setDob(tutors.getDob());
        trainingUsers.setStatus(true);
        trainingUsers.setDesignation(tutors.getDesignation());
        trainingUsers.setAddress(address);

        tutorRepository.save(tutors);
        trainingUsers.setTutorId(tutors.getTutorId());

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
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h2>Welcome to Cluematrix Technology!</h2>" +
                "</div>" +
                "<div class='content'>" +
                "<p>Dear " + tutors.getName() + ",</p>" +
                "<p>Thank you for registering with us. Below are your login details:</p>" +
                "<p><span class='info'>Email:</span> " + tutors.getEmail() + "</p>" +
                "<p><span class='info'>Password:</span> " + tutors.getMobNo() + "</p>" +
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


        userService.sendVerificationEmail(tutors.getEmail(), " Login password", body);
        return tutors;
    }

    @Override
    public Tutors getTutorById(Long tutorId) {
        return tutorRepository.findById(tutorId).get();
    }

    @Override
    public TrainingCourses saveCourse(TrainingCourses coursesEntity, MultipartFile file) throws IOException {
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
            coursesEntity.setCourseImageUrl(fileName);
        }
        // Save the product with the full image URL

        return trainingCourseRepository.save(coursesEntity);
    }

    @Override
    public List<TrainingCourses> getAll() {
        return trainingCourseRepository.findAll();
    }

    @Override
    public List<Tutors> getAllTutors() {
        return tutorRepository.findByStatus(true);
    }

    @Override
    public Tutors uploadTutorImage(Long id, MultipartFile file) throws IOException {
    Tutors tutors=null;
        if (!file.isEmpty()) {
            Path uploadPath = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);
            String fileName = file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            file.transferTo(filePath.toFile()); //save file to the server
            String imageUrl = "/images/" + fileName;
            tutors=  tutorRepository.findById(id).
                    orElseThrow(()-> new RuntimeException("Tutor not Found:"));
            tutors.setImagePath(fileName);
        }
        assert tutors != null;
        return tutorRepository.save(tutors);
    }

    @Override
    public Tutors deactivateUser(Long tutorId) {
    TrainingUsers  users=    trainingUserRepository.findByTutorId(tutorId);
      Tutors tutors=  tutorRepository.findById(users.getTutorId()).orElseThrow(()-> new RuntimeException("Tutor Not found"));
        users.setStatus(!users.isStatus());
        tutors.setStatus(!tutors.isStatus());

        tutorRepository.save(tutors);
        trainingUserRepository.save(users);
    return tutors;
    }

    @Override
    public Tutors editTutor(Long tutorId, TrainingUsers tutors) {
        // Fetch the existing tutor
        Tutors existingTutors = tutorRepository.findById(tutorId)
                .orElseThrow(() -> new RuntimeException("Tutor Not found"));

        if (tutors.getName() != null) {
            existingTutors.setName(tutors.getName());
        }
        if (tutors.getMobNo() != null) {
            existingTutors.setMobNo(tutors.getMobNo());
        }
        if (tutors.getGender() != null) {
            existingTutors.setGender(tutors.getGender());
        }
        if (tutors.getDob() != null) {
            existingTutors.setDob(tutors.getDob());
        }
        if (tutors.getDesignation()!=null){
            existingTutors.setDesignation(tutors.getDesignation());
        }

        tutorRepository.save(existingTutors);

        TrainingUsers existingUser = trainingUserRepository.findByTutorId(tutorId);

        if (tutors.getName() != null) {
            existingUser.setName(tutors.getName());
        }
        if (tutors.getMobNo() != null) {
            existingUser.setMobNo(tutors.getMobNo());
        }
        if (tutors.getDesignation() != null) {
            existingUser.setDesignation(tutors.getDesignation());
        }
        if(tutors.getAddress()!=null){
            existingUser.setAddress(tutors.getAddress());
        }
        trainingUserRepository.save(existingUser);

        return existingTutors;
    }

}
