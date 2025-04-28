package com.skilladmin.repository;

import com.skilladmin.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course,Long> {

    public  Course findByInfoCourse(Long infoCourse);

}
