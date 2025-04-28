package com.skilladmin.repository;

import com.skilladmin.model.TraineeStudentMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudyMaterialRepository extends JpaRepository<TraineeStudentMaterial,Long> {

    @Query(value = "select ts.* from trainee_student_material ts where ts.batch_id=:batchId",nativeQuery = true)
    public List<TraineeStudentMaterial> getStudyMaterialBybatch(Long batchId);
}
