package com.skilladmin.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class TrainingUsers implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long userId;

            private String name;

    private  String email;

    private  Long mobNo;

    private String role;

    private String createdDate;

    private  String gender;
    private Date dob;
    @Column(length = 1500)
    private  String address;
    private Long studentId; // same id from User table
    private Long tutorId;
    private  Long batchId;
    private Long batchfees;
    private  String designation;
    private  String password;

    private String imagePath;
    private boolean status;

    private String notificationToken;

    @PrePersist
    protected  void create(){
        DateTimeFormatter  dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
   this.createdDate= LocalDateTime.now().format(dateTimeFormatter);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return a collection of authorities (roles) granted to the user
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Implement based on your requirements
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Implement based on your requirements
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implement based on your requirements
    }

    @Override
    public boolean isEnabled() {
        return true; // Implement based on your requirements
    }


}
