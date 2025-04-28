package com.skilladmin.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.skilladmin.dto.StudentListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.skilladmin.model.Student;
import com.skilladmin.model.User;
import org.springframework.data.repository.query.Param;


public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query(value = "SELECT * FROM student_data", nativeQuery = true)
    List<Student> getAllStudentsList();


    @Query("SELECT S FROM Student S WHERE " +
            "LOWER(S.student_name) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(S.curr_year) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Student> getFilterStudent(@Param("name") String name, Pageable pageable);


    @Query("SELECT COUNT(s) FROM Student s WHERE DATE(s.creation_date) = DATE(:date)")
    Long countStudentsRegisteredOnDate(@Param("date") Date date);


    @Query(value = "SELECT * FROM student_data ORDER BY student_id DESC LIMIT 5", nativeQuery = true)
    List<Student> getLastStudents();



}

