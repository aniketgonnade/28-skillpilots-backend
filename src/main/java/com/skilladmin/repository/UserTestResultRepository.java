package com.skilladmin.repository;

import com.skilladmin.enumclass.QuestionLevel;
import com.skilladmin.enumclass.TestLevel;
import com.skilladmin.model.UserTestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserTestResultRepository extends JpaRepository<UserTestResult,Long> {

   // boolean existsByUserIdAndTestLevelAndPassed(Long user, TestLevel testLevel, boolean passed);
   boolean existsByUserIdAndTestIdAndQuestionLevelAndPassed(Long userId, Long testId, QuestionLevel questionLevel, boolean passed);

   List<UserTestResult> findByUserId(Long studentId);

   List<UserTestResult> findByUserIdAndTestId(Long studentId, Long id);

   @Query("SELECT u.id, u.username, utp, t.testName " +
           "FROM User u " +
           "JOIN UserTestResult utp ON u.id = utp.userId " +
           "JOIN Test t ON utp.test.id = t.id " +
           "ORDER BY u.id ASC, utp.id DESC")
   List<Object[]> findUserTestAttempts();

}
