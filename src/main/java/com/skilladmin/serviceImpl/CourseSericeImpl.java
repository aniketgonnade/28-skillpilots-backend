package com.skilladmin.serviceImpl;

import com.skilladmin.model.Course;
import com.skilladmin.model.Tutors;
import com.skilladmin.repository.CourseRepository;
import com.skilladmin.repository.TutorRepository;
import com.skilladmin.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CourseSericeImpl implements CourseService {

    public static final String UPLOAD_DIR = "app/images";

    @Autowired
    private TutorRepository tutorRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Override
    public Course saveCourse(Course course, Long infoCourse, Long tutorId) {
        course.setInfoCourse(infoCourse);
        course.setTutorId(tutorId);
        course.setCourseProjects(course.getCourseProjects());
        return courseRepository.save(course);
    }

    @Override
    public Course addCourseImage(Long id, MultipartFile file) throws IOException {

        Course course = courseRepository.findById(id).orElseThrow(() -> new RuntimeException(("Course Not Found with id :" + id)));

        if (!file.isEmpty()) {
            Path paths = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
            Files.createDirectories(paths);
            String name = file.getOriginalFilename();
            Path filePath = paths.resolve(name);
            file.transferTo(filePath.toFile());
            course.setBanner(name);
            courseRepository.save(course);

        }
        return course;
    }

    @Override
    public Object getCourseById(Long id) {
        Tutors tutors = null;
        Course course = courseRepository.findByInfoCourse(id);

        if (course.getTutorId() != null) {
            tutors = tutorRepository.findById(course.getTutorId()).orElseThrow(() -> new RuntimeException("Not found"));
            ;
        }
        List<String> list = new ArrayList<String>();

        list.add(tutors.getName());
        list.add(tutors.getDesignation());
        list.add(tutors.getInfo());
        list.add(tutors.getImagePath());

        assert tutors != null;
        return Map.of("course", course, "tutorInfo", list);
    }

    @Override
    public Course uploadBroucher(Long id, MultipartFile file) throws IOException {
        Course course = courseRepository.findById(id).orElseThrow(() -> new RuntimeException(("Course Not Found with id :" + id)));

        if (!file.isEmpty()) {
            Path paths = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
            Files.createDirectories(paths);
            String name = file.getOriginalFilename();
            Path filePath = paths.resolve(name);
            file.transferTo(filePath.toFile());
            course.setBroucherUrl(name);
            courseRepository.save(course);

        }
        return course;
    }
}
