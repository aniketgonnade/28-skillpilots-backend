package com.skilladmin.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@NoArgsConstructor
@Entity
@Data
public class TraineeAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String assignMentName;
    @Column(length = 800)

    private String description;

    private String file;

    private String createdDate;

    @PrePersist
    public void onCreate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.createdDate = LocalDate.now().format(formatter);
    }

    // private  Long batchId;

    @ManyToOne
    @JoinColumn(name = "batchId")
    @JsonBackReference
    private Batch batch;

    @Override
    public String toString() {
        return "TraineeAssignment{" +
                "id=" + id +
                ", assignMentName='" + assignMentName + '\'' +
                ", description='" + description + '\'' +
                ", file='" + file + '\'' +
                '}';
    }
}
