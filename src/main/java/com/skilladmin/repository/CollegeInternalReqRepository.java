package com.skilladmin.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.skilladmin.model.CollegeInternalRequest;

public interface CollegeInternalReqRepository extends JpaRepository<CollegeInternalRequest, Long> {
	 @Query(value="select cm.company_name,cir.user_id,cir.request_msg,cir.rejection_msg,"
				+ "cir.technology,cir.duration,cir.approval_status,cir.against_ext_req,"
				+ "cir.for_users,cir.creation_date,cir.updation_date,cir.expiration_date,"
				+ "cir.request_id,stdt.test_data_id,stdt.expiration_date as expdate,stdt.status as testStatus "
				+ "FROM coll_internal_req cir INNER JOIN company_data2 cm ON cir.for_company=cm.company_id "
				+ "LEFT JOIN student_test_data stdt ON (stdt.against_ext_req=cir.against_ext_req and stdt.student_id=:student_id) "
				+ "WHERE user_id=:user_id AND ((cir.approval_status<>:approval_status AND "
				+ "(cir.expiration_date>=:expiration_date OR cir.expiration_date is null)) OR cir.approval_status=:approval_status) "
				+ "AND cir.for_users =:for_users ", nativeQuery = true)
	public List<Object[]> getClgInternalReqOfStudentWithDept(@Param("for_users")Long student, Long student_id,@Param("user_id")Long college_id,Date expiration_date,@Param("approval_status") String status);
}
