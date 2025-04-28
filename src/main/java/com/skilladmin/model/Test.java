package com.skilladmin.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.skilladmin.enumclass.TestLevel;
import jakarta.persistence.*;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Test {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String testName;

	private String description;

	private boolean status=true;

	@CurrentTimestamp
	private LocalDateTime creationDate;
	// BASIC, INTERMEDIATE, ADVANCED

	private int timeLimit;
	private int noOfQuestion;
	private int passingMarks;
	private int marksPerQuestion;


	@UpdateTimestamp
	private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Section> sections;

	@Override
	public String toString() {
		return "Test{" +
				"id=" + id +
				", testName='" + testName + '\'' +
				", status=" + status +
				", creationDate=" + creationDate +

				", updatedAt=" + updatedAt +
				", sections=" + sections +
				'}';
	}

//	public int calculateTotalMarks() {
//        return sections.stream().mapToInt(Section::getSectionMarks).sum();
//    }
    
   
}
