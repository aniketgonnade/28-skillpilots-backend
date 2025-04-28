package com.skilladmin.controller;

import com.skilladmin.model.Course;
import com.skilladmin.model.Poster;
import com.skilladmin.service.AssigmentService;
import com.skilladmin.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = {"*"})
@RestController
public class BannerController {
    @Autowired
    private AssigmentService assigmentService;
    @Autowired
    private CourseService courseService;

    @PostMapping("/banner")
    public ResponseEntity<Object> uploadImageManager(
            @RequestParam("file") MultipartFile file) {
        try {
            Poster banner = assigmentService.addBanner(file);
            return ResponseEntity.status(200).body(Map.of("msg", "Image uploaded successfully",
                    "imagePath", banner.getImagePath()));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("msg", "Image upload failed",
                    "error", e.getMessage()));
        }
    }

    @GetMapping("/getBanners")
    public ResponseEntity<Object> getBanners() {
        List<Poster> banners = assigmentService.getAllBaner();
        if (!banners.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("msg", "banners got Succesfully", "status", "200", "asssignMent", banners));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("msg", "Not found", "status", "400"));

        }
    }

    @PostMapping("/saveCourse")
    public ResponseEntity<Course> addCourse(@RequestBody Course course, @RequestParam Long infoCourse, @RequestParam(value = "tutorId", required = false) Long tutorId) {
        Course savedCourse = courseService.saveCourse(course, infoCourse, tutorId);
        return new ResponseEntity<>(savedCourse, HttpStatus.CREATED);
    }

    @PutMapping("/uploadBanner")
    public ResponseEntity<Object> uploadImage(@RequestParam Long id, @RequestParam("file") MultipartFile file) throws IOException {

        Course course = courseService.addCourseImage(id, file);
        if (course != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("msg", "banners add Succesfully", "status", "200", "course", course));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("msg", "banners not add", "status", "500"));
        }
    }

    @GetMapping("/fetchCourse")
    public ResponseEntity<Object> getCourse(@RequestParam Long id) {
        Object course = courseService.getCourseById(id);
        if (course != null) {
            return ResponseEntity.status(HttpStatus.OK).body(course);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Courses Not Found");
        }
    }

    @PostMapping("/uploadBrocher")
    public ResponseEntity<Object> uploadBroucher(@RequestParam Long id, @RequestParam(name = "file") MultipartFile file) throws IOException {
        Course course = courseService.uploadBroucher(id, file);
        if (course != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Map.of("msg", "Brocher add Succesfully", "status", "200", "course", course));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("msg", "Brocher not add", "status", "500"));
        }

    }

}