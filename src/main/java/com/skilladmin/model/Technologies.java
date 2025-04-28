package com.skilladmin.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="technologies")
public class Technologies {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long tech_id;


private String technology_name;



}