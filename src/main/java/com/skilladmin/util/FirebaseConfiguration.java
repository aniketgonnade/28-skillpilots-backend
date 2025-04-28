package com.skilladmin.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;
@Configuration
public class FirebaseConfiguration {
    
	
	@Bean
	public FirebaseApp initializeFirebase() throws IOException {
	    if (FirebaseApp.getApps().isEmpty()) {
	        InputStream serviceAccount = new ClassPathResource("skillpilots-ea967-firebase-adminsdk-ujzh6-b5e1c3ab35.json").getInputStream();
	        FirebaseOptions options = new FirebaseOptions.Builder()
	                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
	                .setStorageBucket("skillpilots-ea967.appspot.com")
	                .build();
	        return FirebaseApp.initializeApp(options);
	    } else {
	        return FirebaseApp.getInstance(); 
	    }
	}
}
