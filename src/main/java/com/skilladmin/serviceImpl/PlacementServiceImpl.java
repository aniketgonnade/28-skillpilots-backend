package com.skilladmin.serviceImpl;


import com.skilladmin.dto.RecruitmentDTO;
import com.skilladmin.model.*;
import com.skilladmin.repository.*;
import com.skilladmin.service.EmailService;
import com.skilladmin.service.PlacementService;
import com.skilladmin.util.FirebaseService;
import com.skilladmin.util.ProjectConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class PlacementServiceImpl implements PlacementService {

    private final RecruitmentRepository placementRepository;
    private final UserRepository userRepository;
    private final RecruitmentDetailsRepo placementDetailsRepo;
    private final EmailService emailService;
    private final RecruitmentStatusRepo recruitmentStatusRepo;
    private final FirebaseService firebaseService;
    private final CompanyDriveRepository companyDriveRepository;

    @Override
    public Recruitment createPlacement(Recruitment placement, RecruitmentDetails placementDetails, String departmentIds, Long collegeId) {
        
        if (placement.getPlacementId() != null) {
            Recruitment recruitment = placementRepository.findById(placement.getPlacementId())
                    .orElseThrow(() -> new RuntimeException("Recruitment Not found"));

            recruitment.setCompanyName(placement.getCompanyName());
            recruitment.setContactNo(placement.getContactNo());
            recruitment.setEmail(placement.getEmail());
            recruitment.setAddress(placement.getAddress());
            recruitment.setProfile(placement.getProfile());
            recruitment.setCtc(placement.getCtc());

            RecruitmentDetails placementDetails1 = recruitment.getPlacementDetails();
            if (placementDetails1 != null) {
                placementDetails1.setJobTitle(placementDetails.getJobTitle());
                placementDetails1.setJobDescription(placementDetails.getJobDescription());
                placementDetails1.setSsc(placementDetails.getSsc());
                placementDetails1.setHsc(placementDetails.getHsc());
                placementDetails1.setUg(placementDetails.getUg());
                placementDetails1.setCity(placementDetails.getCity());
                placementDetails1.setWebsite(placementDetails.getWebsite());
                placementDetails1.setInterviewStartDate(placementDetails.getInterviewStartDate());
                placementDetails1.setInterviewEndDate(placementDetails.getInterviewEndDate());
            }

            recruitment.setDepartmentIds(departmentIds);
            return placementRepository.save(recruitment);
        } else {
            placement.setPlacementDetails(placementDetails);
            placement.setDepartmentIds(departmentIds);
            placement.setCollegeId(collegeId);

            List<User> users = userRepository.findCollegeStudent(Math.toIntExact(collegeId));
            log.info("User List: {}", users);

            int batchSize = 50;
            int counter = 0;

            for (User user : users) {
                String mailBody = "<h1 style=\"font-size:55px;margin:20px;text-align:center\">"
                        + "<span style=\"color:#1974c3;\">Skill<span style=\"color:#01af36;\">Pilots</span></h1>"
                        + "<p style=\"text-align:center\">Dear <strong style=\"color:#414ea4\">" + user.getName() + "</strong>,<br>"
                        + "We are pleased to inform you that a new Recruitment is going on! Log in and apply at <strong style=\"color:#414ea4\"><a href=\"http://www.skillpilots.com\" style=\"color:#414ea4; text-decoration:none;\">SkillPilots</a></strong>.<br>"
                        + "Here are the details regarding your placement opportunities:</p>"
                        + "<ul style=\"text-align:center; list-style-type:none; padding:0;\">"
                        + "<li><strong>Company:</strong> " + placement.getCompanyName() + "</li>"
                        + "</ul>"
                        + "<p style=\"text-align:center\">We encourage you to prepare well for your interview.<br>"
                        + "If you have any questions, feel free to reach out to us at <strong style=\"color:#414ea4\"><a href=\"http://www.skillpilots.com\" style=\"color:#414ea4; text-decoration:none;\">SkillPilots</a></strong>.<br>"
                        + "Best of luck with your placement!</p>"
                        + ProjectConstants.mail_footer;

                emailService.sendVerificationEmail(user.getEmail(), "T and P Placement", mailBody);

                if (user.getNotificationToken() != null && !user.getNotificationToken().isEmpty()) {
                    firebaseService.sendNotification(user.getNotificationToken(), "Placement Update", 
                            placement.getCompanyName() + " is hiring. Go and apply!");
                }

                counter++;

                if (counter % batchSize == 0) {
                    try {
                        Thread.sleep(2000); // Pause for 2 seconds after each batch
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }

            return placementRepository.save(placement);
        }
    }

    @Override
    public Page<Recruitment> getPlacementList(Long collegeId, int page , int size) {
        Pageable pageable = PageRequest.of(page, size);
        return placementRepository.findRecruitmentsByStatusAndCollegeId("A", collegeId,pageable);
    }

    @Override
    public List<Recruitment> getPlacementsByDepartmentId(Long departmentId) {
        return placementRepository.findByDepartmentId(departmentId.toString());
    }

    @Override
    public Recruitment editPlacement(Long placementId, RecruitmentDTO recruitmentDTO) {
        Recruitment existingPlacement = placementRepository.findById(placementId)
                .orElseThrow(() -> new RuntimeException("Placement not found for this id :: " + placementId));

        // Update fields based on the DTO

        // Update department IDs if provided
        if (recruitmentDTO.getDepartmentIds() != null) {
            existingPlacement.setDepartmentIds(recruitmentDTO.getDepartmentIds());
        }

        // Update other fields only if non-null
        if (recruitmentDTO.getCompanyName() != null) {
            existingPlacement.setCompanyName(recruitmentDTO.getCompanyName());
        }
        if (recruitmentDTO.getContactNo() != null) {
            existingPlacement.setContactNo(recruitmentDTO.getContactNo());
        }
        if (recruitmentDTO.getEmail() != null) {
            existingPlacement.setEmail(recruitmentDTO.getEmail());
        }
        if (recruitmentDTO.getAddress() != null) {
            existingPlacement.setAddress(recruitmentDTO.getAddress());
        }
        if (recruitmentDTO.getProfile() != null) {
            existingPlacement.setProfile(recruitmentDTO.getProfile());
        }

        if (recruitmentDTO.getJobTitle() != null) {
            existingPlacement.getPlacementDetails().setJobTitle(recruitmentDTO.getJobTitle());
        }
        if (recruitmentDTO.getJobDescription() != null) {
            existingPlacement.getPlacementDetails().setJobDescription(recruitmentDTO.getJobDescription());
        }
        if (recruitmentDTO.getIsOpen() != null) {
            existingPlacement.getPlacementDetails().setIsOpen(recruitmentDTO.getIsOpen());
        }
        if (recruitmentDTO.getInterviewStartDate() != null) {
            existingPlacement.getPlacementDetails().setInterviewStartDate(recruitmentDTO.getInterviewStartDate());
        }
        if (recruitmentDTO.getInterviewEndDate() != null) {
            existingPlacement.getPlacementDetails().setInterviewEndDate(recruitmentDTO.getInterviewEndDate());
        }
//        if (recruitmentDTO.getCriteria() != null) {
//            existingPlacement.getPlacementDetails().setCriteria(recruitmentDTO.getCriteria());
//        }

        // Save the updated placement to the database
        Recruitment updatedPlacement = placementRepository.save(existingPlacement);

        return updatedPlacement;
    }

    @Override
    public List<Map<String, Object>> findUserRecruitmentDetails(Long collegeId, Long reqruitmentId, String status) {
        return userRepository.findUserRecruitmentDetails(collegeId, reqruitmentId, status);
    }

    @Override
    public List<Map<String, Object>> findByStudentRecruitmentDetails(Long studentId) {
        return userRepository.findByStudentRecruitmentDetails(studentId);
    }

    @Override
    public Recruitment deactivateRecruitment(Long id) {
        Recruitment existingPlacement = placementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Placement not found for this id :: " + id));
        existingPlacement.setStatus("A".equals(existingPlacement.getStatus()) ? "D" : "A");
        return placementRepository.save(existingPlacement);
    }

    @Override
    public List<Map<String, Object>> findCompanyDriveApplyStudents(Long companyId, String jobRole) {
        return userRepository.findCompanyDriveApplyStudents(companyId, jobRole);
    }

    @Override
    public List<CompanyDrive> getFilteredDrives(String search) {
        if (search == null || search.trim().isEmpty()) {
            return List.of(); // Return empty if no search term
        }

        String searchLower = search.toLowerCase();

        return companyDriveRepository.findByFilters(searchLower);
    }

    @Override
    public CompanyDrive getCompanyDriveById(Long driveId) {
        return companyDriveRepository.findById(driveId).orElseThrow(() -> new RuntimeException("Drive Not found" + driveId));
    }

    @Override
    public Optional<Recruitment> getRecruitmentId(Long id) {
        return placementRepository.findById(id);
    }

	@Override
	public List<Recruitment> getAllRecruitmentList() {
		List<Recruitment> list = placementRepository.findAll();
		Collections.reverse(list);
		return list;
	}
}
