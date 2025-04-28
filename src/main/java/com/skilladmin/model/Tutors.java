package com.skilladmin.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "traningtutors")
public class Tutors {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long tutorId;

    private  String name;
    @Email(message = "Email should be valid")
    private  String email;
    private  String designation;
    private String date;
    private  String gender;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dob;




    private String password;
    private Long mobNo;

    private String imagePath;
    private boolean status=true;

    @Column(length = 1000)
    private String info;

    @OneToMany(mappedBy = "tutors", fetch =FetchType.LAZY ,cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Batch> batches= new ArrayList<>();




         @PrePersist
         protected  void onCreate(){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            this.date= LocalDateTime.now().format(dateTimeFormatter);
        }




}
