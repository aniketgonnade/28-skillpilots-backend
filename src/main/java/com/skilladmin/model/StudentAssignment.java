package com.skilladmin.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class StudentAssignment {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @ManyToOne
	    @JoinColumn(name = "assignment_id")
	    private TraineeAssignment assignment;
	    
	    private String solvedFile;  // Path to student's uploaded file

	    private LocalDate uploadDate;

		private  Long studentId;
		private  Long batchId;
		@Column(length = 1000)
		private String description;



}
