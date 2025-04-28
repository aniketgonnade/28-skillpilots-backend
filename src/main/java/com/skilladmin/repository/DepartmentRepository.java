package com.skilladmin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skilladmin.model.Department;
import org.springframework.data.jpa.repository.Query;


public interface DepartmentRepository extends JpaRepository<Department, Long> {

	Optional<Department> findById(Long dept);
	@Query(value = "select d.dept_id,d.dept_name from department_data d left join coll_dept_link cd" +
			" on d.dept_id=cd.dept_id where cd.college_id=:collegeId",nativeQuery = true)
	List<Object[]> getDept(Long collegeId);
	
	
	

}
