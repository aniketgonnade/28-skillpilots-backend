package com.skilladmin.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Poster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long bannerId;
    private  String imagePath;
}
