package com.skilladmin.service;


import java.util.List;
import java.util.Optional;

import com.skilladmin.model.Department;

public interface DepartmentService {
	
	
	public Department createDepartment( Department department) ;
	
	public List<Department> getDepartments();
	
	public Optional<Department> findById(Long deptId);
	
	public void deleteDept(Long deptId);
		
	

}
