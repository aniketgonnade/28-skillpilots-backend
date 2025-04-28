package com.skilladmin.service;


public interface EmailService {


    void sendVerificationEmail(String toEmail, String subject, String content) ;

}
