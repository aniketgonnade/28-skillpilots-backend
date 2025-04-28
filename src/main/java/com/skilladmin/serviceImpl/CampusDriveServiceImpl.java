package com.skilladmin.serviceImpl;

import com.skilladmin.model.CampusDrive;
import com.skilladmin.repository.CampusDriveRepository;
import com.skilladmin.service.CampusDriveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampusDriveServiceImpl implements CampusDriveService {
    @Autowired
    private CampusDriveRepository campusDriveRepository;
    @Override
    public CampusDrive register(CampusDrive campusDrive) {
        return campusDriveRepository.save(campusDrive);
    }


}
