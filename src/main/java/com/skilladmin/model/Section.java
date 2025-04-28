package com.skilladmin.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
@Entity
@Data
public class Section {

	
	    public Section(Object object, String sectionName2, int timeLimit2, int sectionMarks2, ArrayList arrayList) {
	}

		public Section() {

		}

		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String sectionName;


	    @ManyToOne
	    @JoinColumn(name = "test_id", nullable = false)
	    @JsonBackReference
	    private Test test;

	    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
	    @JsonManagedReference
	    private List<Question> questions= new ArrayList<>();;


	    @CreationTimestamp
	    private LocalDateTime createdAt;

	    @UpdateTimestamp
	    private LocalDateTime updatedAt;

	@Override
	public String toString() {
		return "Section{" +
				"id=" + id +
				", sectionName='" + sectionName + '\'' +

				", test=" + test +
				", questions=" + questions +
				", createdAt=" + createdAt +
				", updatedAt=" + updatedAt +
				'}';
	}

	public Section(Test test, String sectionName) {
		this.test = test;
		this.sectionName = sectionName;

		this.questions = new ArrayList<>();
	}
}
