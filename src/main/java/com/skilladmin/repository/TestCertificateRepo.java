package com.skilladmin.repository;

import com.skilladmin.model.TestCertificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestCertificateRepo extends JpaRepository<TestCertificate,Long> {

    List<TestCertificate> findAllByUserIdAndTestId(Long userId, Long testId);

    TestCertificate findByUserId(Long userId);


}
