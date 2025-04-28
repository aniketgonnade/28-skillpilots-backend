package com.skilladmin.service;



import com.skilladmin.dto.RecruitmentDTO;
import com.skilladmin.model.CompanyDrive;
import com.skilladmin.model.Recruitment;
import com.skilladmin.model.RecruitmentDetails;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PlacementService {

    public Recruitment createPlacement(Recruitment placement, RecruitmentDetails placementDetails, String departmentIds, Long collegeId);

    public Page<Recruitment> getPlacementList(Long collegeId, int page , int size);

    public List<Recruitment> getPlacementsByDepartmentId(Long departmentId);

    public Recruitment editPlacement(Long placementId, RecruitmentDTO recruitmentDTO);

    List<Map<String, Object>> findUserRecruitmentDetails(Long collegeId,Long reqruitmentId,String status);

    List<Map<String, Object>> findByStudentRecruitmentDetails(Long studentId);

    public  Recruitment deactivateRecruitment(Long id);

    List<Map<String, Object>> findCompanyDriveApplyStudents(Long companyId,String jobRole);

    public List<CompanyDrive> getFilteredDrives(String search);

    public CompanyDrive getCompanyDriveById(Long driveId);

    public Optional<Recruitment> getRecruitmentId(Long id);
    
    public List<Recruitment> getAllRecruitmentList();
}
