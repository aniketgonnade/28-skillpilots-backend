package com.skilladmin.repository;

import com.skilladmin.model.TestCertificate;
import org.springframework.data.jpa.repository.JpaRepository;

import com.skilladmin.model.Test;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TestRepository extends JpaRepository<Test, Long> {


//    @Query("SELECT t FROM Test t WHERE LOWER(t.testName) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY t.testName ASC")
//    List<Test> findByTestNameLike(@Param("name") String name);

    @Query("SELECT t FROM Test t WHERE LOWER(t.testName) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY t.testName ASC")
    List<Test> findByTestNameLike(@Param("name") String name);

    @Query("SELECT t FROM Test t WHERE t.status = true ORDER BY CASE WHEN LOWER(t.testName) LIKE LOWER(CONCAT('%', :name, '%')) THEN 0 ELSE 1 END, t.testName ASC")
    List<Test> findByTestNamePriority(@Param("name") String name);

}