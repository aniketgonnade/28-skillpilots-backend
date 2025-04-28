package com.skilladmin.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skilladmin.enumclass.QuestionLevel;
import com.skilladmin.enumclass.QuestionType;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Question {

	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Lob
		@Column(columnDefinition = "TEXT")
	    private String questionText;
	    private String optionA;
	    private String optionB;
	    private String optionC;
	    private String optionD;

		private Long testId;
	    @Enumerated(EnumType.STRING)
	    private QuestionType questionType;

		
	    @ElementCollection
	    private List<String> correctAnswers;// Supports multiple correct answers

		@Enumerated(EnumType.STRING)
		private QuestionLevel questionLevel;

		private String answer; // Supports single answer cases

	    private int marks; // Marks for this question

	    @ManyToOne
	    @JoinColumn(name = "section_id", nullable = false)
	    @JsonBackReference
	    private Section section;

	@Override
	public String toString() {
		return "Question{id=" + id + ", questionText='" + questionText + "', questionType=" + questionType + "}";
	}


}
