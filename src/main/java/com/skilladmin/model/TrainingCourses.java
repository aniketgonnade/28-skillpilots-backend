package com.skilladmin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class TrainingCourses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long courseId;

    @NonNull
    @Column()
    private String courseName;

    @NonNull
    private String courseDuration;

    private String courseImageUrl;

    @NonNull
    private String courseDescription;



}
