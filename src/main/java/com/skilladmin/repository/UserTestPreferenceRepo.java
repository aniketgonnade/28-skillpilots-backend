package com.skilladmin.repository;

import com.skilladmin.model.UserTestPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserTestPreferenceRepo extends JpaRepository<UserTestPreference,Long> {

   List<UserTestPreference> findByStudentId(Long studentId);
   @Query("SELECT u FROM UserTestPreference u WHERE u.studentId = :studentId")
   public UserTestPreference getStudent(@Param("studentId") Long studentId);



}
