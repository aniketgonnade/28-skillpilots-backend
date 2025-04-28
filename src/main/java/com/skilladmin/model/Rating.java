package com.skilladmin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Data 
@AllArgsConstructor
@NoArgsConstructor
public class Rating {

	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   
    private String param1; // Very Good, Good, Fair, Poor, Very Poor

   
    private String param2; // Very Good, Good, Fair, Poor, Very Poor

  
    private String param3; // Very Good, Good, Fair, Poor, Very Poor

    private String param4;
    private String param5;

    @Column(columnDefinition = "TEXT")
    private String suggestion; // Last suggestion or feedback message

    private Long userId;
    private String createdDate;

    @PrePersist
    public void onCreate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.createdDate = LocalDate.now().format(formatter);
    }

}
