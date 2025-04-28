package com.skilladmin.repository;

import com.skilladmin.model.TrainingCourses;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingCourseRepository extends JpaRepository<TrainingCourses,Long> {
}
