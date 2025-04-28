package com.skilladmin.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skilladmin.enumclass.QuestionType;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
@Data
@Entity
public class Questions {
	
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    @Column(name = "question_id")
	    private Long id;
        
	    @Column(length = 2000)
	    private String text;

	    private String option1;
	    private String option2;
	    private String option3;
	    private String option4;

	    private String correctAnswer;
	   
	    @ElementCollection
	    private List<String> correctAnswers;  // To support multiple correct answers

	    private int marksques;  // Marks for the question

	    @Enumerated(EnumType.STRING)
	    private QuestionType type;  // Type of the question

	    private String difficultyLevel;  // Difficulty level of the question

	    private Long examId;
	    
	    
	    @JsonIgnore
	    @ManyToOne
	    @JoinColumn(name = "testId", nullable = false)
	    private AssessmentTest assessmentTest;

	    @Override
	    public String toString() {
	        StringBuilder sb = new StringBuilder();
	        sb.append("Questions [id=").append(id)
	          .append(", text=").append(text)
	          .append(", option1=").append(option1)
	          .append(", option2=").append(option2)
	          .append(", option3=").append(option3)
	          .append(", option4=").append(option4)
	          .append(", correctAnswer=").append(correctAnswer)
	          .append("]");
	        return sb.toString();
	    }
}
