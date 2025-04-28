package com.skilladmin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SessionFeedback {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String college;
    private String department;
    private String year;
    private String email;
    private String contact;
    private String rating;
    private String relevance;
    private String engagement;
    private String clarity;
    private String confidence;

    @ElementCollection
    private List<String> helpfulParts;

    private String applyInsights;
    private String improvements;
    private String recommend;
    private String comments;

    @CreationTimestamp
    private LocalDateTime createdDate;
}
