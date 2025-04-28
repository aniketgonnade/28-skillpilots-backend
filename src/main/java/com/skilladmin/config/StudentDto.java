package com.skilladmin.config;

import com.skilladmin.model.Student;
import com.skilladmin.model.StudentPast;
import com.skilladmin.model.TrainingUsers;
import com.skilladmin.model.User;

import lombok.Data;

@Data
public class StudentDto {
	private String message;
	private String token;
	private String contact;
	private int statusCode;
	
	private String email;
	private String password;
	private User user;
	private Student student;
    private StudentPast studentPast;

    private String notificationToken;

    private boolean isExists;

	private boolean loginStatus;
	private TrainingUsers trainingUsers;
}
