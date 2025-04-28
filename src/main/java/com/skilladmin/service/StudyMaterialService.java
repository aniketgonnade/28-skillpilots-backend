package com.skilladmin.service;

import com.skilladmin.model.TraineeStudentMaterial;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StudyMaterialService {

    public TraineeStudentMaterial
    createStudyMatrial(TraineeStudentMaterial studentMaterial, MultipartFile file) throws IOException;

    public void  deleteById(Long id);

}
