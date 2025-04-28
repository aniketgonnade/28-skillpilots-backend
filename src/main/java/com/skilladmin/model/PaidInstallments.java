package com.skilladmin.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Data;

import java.time.LocalDate;
import java.util.Random;

@Entity
@Data
public class PaidInstallments {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	 
	    private String studentId;

	
	    private Double paid;

	   
	    private String mode;

	
	    private String instId;

	  
	    private String batchName; // Updated to include batchName

	
	    private Double batchFees;

	   
	    private Double balance; // Updated to include balance

	
	    private LocalDate date; // Field for payment date
	    
	    private Long rid;
	    @PrePersist
	    private void generateRid() {
	        // Example: Generate a random receipt ID (you can customize this logic)
	        this.rid = new Random().nextLong(100000); // Generates a random ID between 0 and 99999
	    }
}
