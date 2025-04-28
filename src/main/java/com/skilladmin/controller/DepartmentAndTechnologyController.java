package com.skilladmin.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skilladmin.model.CompAdvertisement;
import com.skilladmin.model.Department;
import com.skilladmin.model.Technologies;
import com.skilladmin.repository.CompAdvertisementRepo;
import com.skilladmin.service.CompAdvertismentService;
import com.skilladmin.service.DepartmentService;
import com.skilladmin.service.TechnologyService;

@RestController
@CrossOrigin(origins = {"*"})
public class DepartmentAndTechnologyController {
	
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private TechnologyService technologyService;
	@Autowired
	private CompAdvertismentService advertismentService;
	@Autowired
	private CompAdvertisementRepo advertisementRepo;
	
	@PostMapping("/createDept")
	public ResponseEntity<Object> createDepartment(@RequestBody Department department){
		
		HashMap<String, Object> response = new HashMap<>();
		
		Department savedDepartment = departmentService.createDepartment(department);
		
		response.put("msg", "Department save");
		response.put("department", savedDepartment);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
		
	}
	
	@PostMapping("/createTech")
	public ResponseEntity<Object>createTechnology(@RequestBody Technologies technologies){
		HashMap<String, Object> response = new HashMap<>();

		Technologies createTechnologies = technologyService.createTechnologies(technologies);
		
		response.put("msg", "Technologies save");
		response.put("Technologies", createTechnologies);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
	
	@GetMapping("/getDept")
	public ResponseEntity<Object> getDept(){
		HashMap<String, Object> response = new HashMap<>();
		List<Department> departments = departmentService.getDepartments();
		if(departments!=null) {
			response.put("msg", "Success");
			response.put("DeptList", departments);
		}
		else
		{
			response.put("msg", "Not Found");

		}
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
	
	@GetMapping("/getTech")
	public ResponseEntity<Object> getTechnologies(){
		HashMap<String, Object> response = new HashMap<>();
		List<Technologies> technologies = technologyService.getTechnologies();
		if(technologies!= null) {
			response.put("msg", "Success");
			response.put("technologies", technologies);
		}
		else
		{
			response.put("msg", "Not Found");

		}
		return new ResponseEntity<Object>(response, HttpStatus.OK);

		
	}
	
	@DeleteMapping("/deleteTech")
	public ResponseEntity<Object> deleteTechnology(@RequestParam("tech_id") Long tech_id){
		HashMap<String, Object> response = new HashMap<>();
		Technologies technologies = technologyService.findTechnologyById(tech_id);
		if(technologies!=null) {
			technologyService.deleteById(tech_id);
			response.put("msg", "Deleted");
			
		}
		else {
			response.put("msg", "Not found");

		}
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
	
	@DeleteMapping("/deleteDept")
	public ResponseEntity<String> deleteDepartment(@RequestParam("deptId") Long deptId){
		return departmentService.findById(deptId).map(department->{
			departmentService.deleteDept(deptId);
			return ResponseEntity.ok("Department deleted successfully");
		})
        .orElseGet(() -> ResponseEntity.status(404).body("Department not found"));

	}
	
	@PutMapping("/editDept")
	public ResponseEntity<Object> editDepartment(@RequestParam Long deptId,@RequestBody Department departments){
		HashMap<String, Object> response = new HashMap<>();
		Optional<Department> department = departmentService.findById(deptId);
		if(!department.isEmpty()) {
			Department department2 = department.get();
			department2.setDept_name(departments.getDept_name());
			department2.setDept_type(departments.getDept_type());
			department2.setUpdationDate(new Date());
			departmentService.createDepartment(department2);
			response.put("msg", "Succes");
			response.put("department", department2);

		}
		else {
			response.put("msg", "Not found");
		}
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
	
	@PutMapping("/editTech")
	public ResponseEntity<Object> editTechnology(@RequestParam Long tech_id, @RequestBody Technologies technologies){
		HashMap<String, Object> response = new HashMap<>();
		Technologies technology = technologyService.findTechnologyById(tech_id);
		if(!(technology==null))
				{
			technology.setTechnology_name(technologies.getTechnology_name());
			technologyService.createTechnologies(technology);
			response.put("msg", "Succes");
			response.put("technology", technology);

				
				}
		else {
			response.put("msg", "Not found");

		}
	
		return new ResponseEntity<Object>(response, HttpStatus.OK);

	}
	
	@GetMapping("/api/tech")
	public ResponseEntity<Object> tech(){
		HashMap<String, Object> response = new HashMap<>();
		List<String>tech = advertisementRepo.getTechnology();
		if(!tech.isEmpty()) {
			response.put("msg", "technology");
			response.put("tech", tech);
			return new ResponseEntity<Object>(response, HttpStatus.OK);

		}
		else {
			response.put("msg", "not found");
			response.put("tech", tech);
			return new ResponseEntity<Object>(response, HttpStatus.OK);

		}
		}
	
	@GetMapping("/api/location")
	public ResponseEntity<Object> location(){
		HashMap<String, Object> response = new HashMap<>();
		List<String> location= advertisementRepo.getLocation();
		if(!location.isEmpty()) {
			response.put("msg", "location");
			response.put("location", location);
			return new ResponseEntity<Object>(response, HttpStatus.OK);

		}
		else {
			response.put("msg", "not found");
			response.put("location", location);
			return new ResponseEntity<Object>(response, HttpStatus.OK);

		}
		}

}
	
