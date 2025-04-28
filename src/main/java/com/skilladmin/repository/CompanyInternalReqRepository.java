package com.skilladmin.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.skilladmin.model.CompanyInternalRequest;

public interface CompanyInternalReqRepository extends JpaRepository<CompanyInternalRequest, Long> {

	 @Query(value="select * from comp_internal_req where against_ext_req=:external_req_id",nativeQuery = true)
		CompanyInternalRequest getCompIntReqFromExtReqId(@Param("external_req_id") Long against_request);

}
