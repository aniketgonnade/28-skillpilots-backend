package com.skilladmin.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skilladmin.model.Department;
import com.skilladmin.repository.DepartmentRepository;
import com.skilladmin.service.DepartmentService;
@Service
public class DepartmentServiceImpl implements DepartmentService {
	@Autowired
	private DepartmentRepository departmentRepository;
	
	
	@Override
	public Department createDepartment(Department department) {
		return departmentRepository.save(department);
	}

	@Override
	public List<Department> getDepartments() {
		return departmentRepository.findAll();
	}

	@Override
	public Optional<Department> findById(Long deptId) {
		return departmentRepository.findById(deptId);
	}

	@Override
	public void deleteDept(Long deptId) {
		departmentRepository.deleteById(deptId);
	}

}
