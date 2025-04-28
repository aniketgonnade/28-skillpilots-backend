package com.skilladmin.service;

import com.skilladmin.model.Course;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CourseService {

    public Course saveCourse(Course course,Long infoCourse,Long tutorId);

    public Course addCourseImage(Long id, MultipartFile file) throws IOException;

    public Object getCourseById(Long id);

    Course uploadBroucher(Long id, MultipartFile file) throws IOException;
}
