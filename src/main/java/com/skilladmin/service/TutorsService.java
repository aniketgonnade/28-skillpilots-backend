package com.skilladmin.service;

import com.skilladmin.model.TrainingCourses;
import com.skilladmin.model.TrainingUsers;
import com.skilladmin.model.Tutors;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TutorsService {

    public Tutors create(List<Long> batchIds, Tutors  tutors,String address);

    public Tutors getTutorById(Long tutorId);
    public TrainingCourses saveCourse(TrainingCourses coursesEntity, MultipartFile file) throws IOException;

    public  List<TrainingCourses> getAll();

    public List<Tutors> getAllTutors();
    public Tutors uploadTutorImage(Long id, MultipartFile file) throws IOException;

    public Tutors deactivateUser(Long tutorId);

    public Tutors editTutor(Long tutorId, TrainingUsers tutors);

}
