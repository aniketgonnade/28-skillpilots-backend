package com.skilladmin.dto;

import com.skilladmin.model.Student;
import com.skilladmin.model.StudentPast;
import com.skilladmin.model.User;

import lombok.Data;

@Data
public class RegistrationRequestDto {
	 private Student student;
	    private User user;
	    private StudentPast studentPast;
	    private String role;
	   // private int collegeId;
	   // private Long id;
}
