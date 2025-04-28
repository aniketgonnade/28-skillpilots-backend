package com.skilladmin.serviceImpl;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skilladmin.model.College;
import com.skilladmin.model.Company;
import com.skilladmin.model.User;
import com.skilladmin.repository.CollegeRepository;
import com.skilladmin.repository.UserRepository;
import com.skilladmin.service.CollegeService;
import com.skilladmin.service.UserService;
@Service
public class CollegeServiceImpl implements CollegeService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private CollegeRepository collegeRepository;

	@Override
	public List<Object[]> getAllColleges() {
		return userRepository.findAllCollege();
	}

	@Override
	public List<Object[]> getAllCompanies() {
		return userRepository.findAllCompany();
	}

	@Override
	public User updateCollege(Long college_id) {
		User college = userRepository.findById(college_id)
				.orElseThrow(() -> new RuntimeException("Not found: " + college_id));

		Random random = new Random();

		// Toggle verified status: if 1, set to 0; if 0, set to 1
		if (college.getVerified() == 1) {
			college.setVerified(0);
		} else {
			college.setVerified(1);
			String password = "CLIOCOL" + random.nextInt(1000);
			password = password.replaceAll("\\s", "");
			System.out.println("Password: " + password);

			college.setPassword(password);

			String body1 = "<h1 style=\"font-size:55px;margin:20px;text-align:center\">Welcome to <span style=\"color:#1974c3;\">Skill<span style=\"color:#01af36;\">Pilots</span></h1>"
					+ "Your login has been created for your College Profile - " + college.getUsername() + "."
					+ "<p style=\"text-align:center\"> Here is your temporary password - <strong>" + password
					+ "</strong><br>" + "Click <a href='https://skillpilots.com/'><strong>here</strong></a> to complete your registration.<br>"
					+ "You'll be asked to change your password for security reasons.<br><br>"
					+ "Thanks & Regards,<br>SkillPilots Team</p>"
					+ "<p style=\"text-align:justify;font-size:12px;\">Note: This is a system generated mail, please do not reply to it. This mailbox isn't being monitored.<br>"
					+ "The information contained in this e-mail and/or attachments to it contain confidential or privileged information."
					+ "If you are not the intended recipient, any dissemination, use, review, distribution, printing, or copying of the"
					+ " information contained in this e-mail message and/or attachments to it are strictly prohibited."
					+ "If you have received this communication in error, please notify us at +91712-2241405.</p>";

			// Send email only when verified is set to 1
			userService.sendVerificationEmail(college.getEmail(), "Temporary login password", body1);
		}

		userService.saveUser(college);

		// Handle the HOD (Head of Department) verification
		User hod = userService.getHodByInstituteId(college.getInstituteId());
		String passRole1 = "CLIOCOL1" + random.nextInt(1000);
		passRole1 = passRole1.replaceAll("\\s", "");

		if (hod.getVerified() == 0) {
			hod.setVerified(1);
			hod.setPassword(passRole1);

			String body = "<h1 style=\"font-size:55px;margin:20px;text-align:center\">Welcome to <span style=\"color:#1974c3;\">Skill<span style=\"color:#01af36;\">Pilots</span></h1>"
					+ "Your login has been created by College for T&P - " + hod.getUsername() + "."
					+ "<p style=\"text-align:center\"> Here is your temporary password - <strong>" + passRole1
					+ "</strong><br>" + "Click <a href='https://skillpilots.com/'><strong>here</strong></a> to complete your registration.<br>"
					+ "You'll be asked to change your password for security reasons.<br><br>"
					+ "Thanks & Regards,<br>SkillPilots Team</p>"
					+ "<p style=\"text-align:justify;font-size:12px;\">Note: This is a system generated mail, please do not reply to it. This mailbox isn't being monitored.<br>"
					+ "The information contained in this e-mail and/or attachments to it contain confidential or privileged information."
					+ "If you are not the intended recipient, any dissemination, use, review, distribution, printing, or copying of the"
					+ " information contained in this e-mail message and/or attachments to it are strictly prohibited."
					+ "If you have received this communication in error, please notify us at +91712-2241405.</p>";

			// Send email only when verified is set to 1 for HOD
			userService.sendVerificationEmail(hod.getEmail(), "Temporary login password", body);
		}else{
			hod.setVerified(0);
		}

		userService.saveUser(hod);

		return college;
	}


	@Override
	public User updateCompany(Long company_id) {
	    Random random = new Random();

	    // Fetch the company
	    User company = userRepository.findById(company_id)
	        .orElseThrow(() -> new RuntimeException("Not found: " + company_id));
	    
	    // Generate a temporary password for the company
	    String password = "SkillpCmp" + random.nextInt(1000);
	    password = password.replaceAll("\\s", "");

	    // Print company info for debugging
	    System.out.println("Company: " + company);

	    // Check and update verification status for the company
	    if (company.getVerified() == 0) {
	        company.setVerified(1);
	        company.setPassword(password);

	        // Prepare the email body for the company
	        String body1 = "<h1 style=\"font-size:55px;margin:20px;text-align:center\">Welcome to <span style=\"color:#1974c3;\">Skill<span style=\"color:#01af36;\">Pilots</span></h1>"
	                + "Your login has been created for your Company Profile - " + company.getUsername() + "."
	                + "<p style=\"text-align:center\"> Here is your temporary password - <strong>" + password
	                + "</strong><br>" + "Click <a href='https://skillpilots.com/'><strong>here</strong></a> to complete your registration.<br>"
	                + "You'll be asked to change your password for security reasons.<br><br>"
	                + "Thanks & Regards,<br>SkillPilots Team</p>"
	                + "<p style=\"text-align:justify;font-size:12px;\">Note: This is a system-generated mail, please do not reply to it. This mailbox isn't being monitored.<br>"
	                + "The information contained in this e-mail and/or attachments to it contain confidential or privileged information."
	                + "If you are not the intended recipient, any dissemination, use, review, distribution, printing, or copying of the"
	                + " information contained in this e-mail message and/or attachments to it are strictly prohibited."
	                + "If you have received this communication in error, please notify us at +91-9422926439.</p>";

	        // Send the verification email
	        userService.sendVerificationEmail(company.getEmail(), "Temporary login password", body1);
	    } else if (company.getVerified() == 1) {
	        // If setting to 0 (deactivating), do not send an email
	        company.setVerified(0);
	        System.out.println("Company deactivated: " + company.getUsername());
	    }

	    // Save the company
	    userService.saveUser(company);

	    // Fetch HR by institute ID
	    User hr = userService.getHrByInstituteId(company.getInstituteId());
	    System.out.println("HR: " + hr);

	    // Generate a temporary password for HR
	    String passRole1 = "CLIOCOM1" + random.nextInt(1000);
	    passRole1 = passRole1.replaceAll("\\s", "");

	    // Check and update verification status for HR
	    if (hr.getVerified() == 0) {
	        hr.setVerified(1);
	        hr.setPassword(passRole1);

	        // Prepare the email body for HR
	        String body = "<h1 style=\"font-size:55px;margin:20px;text-align:center\">Welcome to <span style=\"color:#1974c3;\">Skill<span style=\"color:#01af36;\">Pilots</span></h1>"
	                + "Your login has been created by your Company for HR - " + hr.getUsername() + "."
	                + "<p style=\"text-align:center\"> Here is your temporary password - <strong>" + passRole1
	                + "</strong><br>" + "Click <a href='https://skillpilots.com/'><strong>here</strong></a> to complete your registration.<br>"
	                + "You'll be asked to change your password for security reasons.<br><br>"
	                + "Thanks & Regards,<br>SkillPilots Team</p>"
	                + "<p style=\"text-align:justify;font-size:12px;\">Note: This is a system-generated mail, please do not reply to it. This mailbox isn't being monitored.<br>"
	                + "The information contained in this e-mail and/or attachments to it contain confidential or privileged information."
	                + "If you are not the intended recipient, any dissemination, use, review, distribution, printing, or copying of the"
	                + " information contained in this e-mail message and/or attachments to it are strictly prohibited."
	                + "If you have received this communication in error, please notify us at +91-9422926439.</p>";

	        // Send the verification email to HR
	        userService.sendVerificationEmail(hr.getEmail(), "Temporary login password", body);
	    } else if (hr.getVerified() == 1) {
	        // If setting to 0 (deactivating), do not send an email
	        hr.setVerified(0);
	        System.out.println("HR deactivated: " + hr.getUsername());
	    }

	    // Save the HR user
	    userService.saveUser(hr);

	    return company;
	}


	@Override
	public List<Object[]> getStudentsWithCollege(String role, Long college_id) {
		if(!(college_id==0)) {
			return userRepository.getStudentWithCollege(role, college_id);
		}
		else {
			return userRepository.getExternalStudents(role);
		}
	}

	@Override
	public College getCollegeInfo(String email) {
		return collegeRepository.findByEmail(email);
	}

	@Override
	public List<College> getColleges() {
		return collegeRepository.findAll();
	}
}
