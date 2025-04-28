package com.skilladmin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String banner;
    private String courseName;
    private String courseTitle;
    private String courseDesc;
    private String techYouMasters;

    private Long infoCourse;

    private Long tutorId;

    private String broucherUrl;


@JsonManagedReference
@OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> courseProjects = new ArrayList<>();;


    @Embedded
    private CourseJourney courseJourney;



    @ElementCollection
    private List<String> programOutcomes;
    @ElementCollection
    private List<String> whatYouLearn;

}
