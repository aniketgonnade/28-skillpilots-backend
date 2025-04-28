package com.skilladmin.dto;

import com.skilladmin.model.TrainingUsers;
import lombok.Data;

@Data
public class LoginRequest {


    private  String email;
    private  String password;
    private  String msg;
    private  String status;
    private  String token;
    private TrainingUsers trainingUsers;

}

