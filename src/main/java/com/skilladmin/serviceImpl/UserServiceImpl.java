package com.skilladmin.serviceImpl;

import com.skilladmin.dto.AdminDto;
import com.skilladmin.model.Tutors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.skilladmin.model.User;
import com.skilladmin.repository.UserRepository;
import com.skilladmin.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserRepository userRepository;
    public static final String UPLOAD_DIR = "src/main/resources/static/images/";


    @Override
    public void sendVerificationEmail(String toEmail, String subject, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject(subject);

            String emailContent = content;

            helper.setText(emailContent, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getHodByInstituteId(Long instituteId) {
        return userRepository.findByCommanId2(instituteId);
    }

    @Override
    public User getHrByInstituteId(Long instituteId) {
        return userRepository.findByCommanId(instituteId);
    }

    @Override
    public Long getStatus(Long id) {
        return userRepository.findStatus(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public int findByHodVerified(Long studentId) {
        return userRepository.findByHodVerified(studentId);
    }

    @Override
    public List<Map<String, Object>> findManagerAndLead(Long companyId) {
        return userRepository.findManagerAndLead(companyId);
    }

    @Override
    public User editAdmin(Long adminId, AdminDto adminDto) {
        User admin = userRepository.findById(adminId).orElseThrow(() -> new RuntimeException("Admin not Found>>" + adminId));
        if (adminDto.getContactNo() != null) {
            admin.setContact_no(adminDto.getContactNo());
        }
        if (adminDto.getUsername() != null) {
            admin.setUsername(adminDto.getUsername());
        }
        return userRepository.save(admin);
    }

	@Override
	public List<Map<String, Object>> getUsersWithRole20() {
		 List<Object[]> results = userRepository.findUsersWithRole20();
	        List<Map<String, Object>> usersList = new ArrayList<>();
	       
	        for (Object[] row : results)
	        {
	            Map<String, Object> userMap = new LinkedHashMap<>();
	            userMap.put("username", row[0] != null ? row[0] : "null");
	            userMap.put("email", row[1] != null ? row[1] : "null");
	            userMap.put("gender", row[2] != null ? row[2] : "null");
	            userMap.put("contact_no", row[3] != null ? row[3] : 0L);
	            userMap.put("role", row[4] != null ? row[4] : "null");
	            usersList.add(userMap);
	        }
	        return usersList;

	}
}


