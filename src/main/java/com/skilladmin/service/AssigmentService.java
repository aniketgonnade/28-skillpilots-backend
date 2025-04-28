package com.skilladmin.service;

import com.skilladmin.model.Poster;
import com.skilladmin.model.Batch;
import com.skilladmin.model.StudentAssignment;
import com.skilladmin.model.TraineeAssignment;
import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AssigmentService {

    public TraineeAssignment addAssignment(TraineeAssignment traineeAssignment, MultipartFile file)  throws IOException;

   public Batch assignAssignMentToBach(Long batchId, Long assignMentId);

   public List<TraineeAssignment> getAssignmentByBatchId(Long batchId);

   public Poster addBanner(MultipartFile file) throws IOException;

   public List<Poster> getAllBaner();

   public StudentAssignment uploadSolvedAssinment(Long studentId,Long batchId,Long assignMentId,MultipartFile file,String description) throws IOException;

   public List<Map<String, Object>> getStudentAssignmentWithTrainingUser(Long batchId);

   public List<StudentAssignment> getStudentAssignmentForStudent(Long studentId);

   public void  deleteById(Long  id);

   public void deleteSolveAssignment(Long id);

}
