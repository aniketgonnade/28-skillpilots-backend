package com.skilladmin.service;

import com.skilladmin.dto.BatchAndFees;
import com.skilladmin.dto.LoginRequest;
import com.skilladmin.dto.TraineeStudentRegistration;
import com.skilladmin.dto.TraineeUpdateDto;
import com.skilladmin.model.TrainingUsers;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TrainingUserService {

    public TrainingUsers  craeteTraingStudents(@RequestBody TraineeStudentRegistration registration);

    public  LoginRequest login (@RequestBody LoginRequest loginRequest);


    public List<TrainingUsers> getStudensBatchVice(Long batchId);

    public TrainingUsers addManager(TrainingUsers trainingUsers);

    public Map<String, Object> getStudent(Long studentId);
    public List<Object[]> getAllTraiStudents();

    public  TrainingUsers getTrainingUserById(Long id);

    public  TrainingUsers uploadPhotoManager(Long id, MultipartFile file) throws IOException;

    public  List<TrainingUsers> getAllUser();

    public  TrainingUsers updateStudent(Long studentId, TraineeUpdateDto trainingUsers);

    public void deleteStudent(Long studentId);

    public TrainingUsers deactivateUser(Long userId);

    public  TrainingUsers activateUser(Long userId);
    public List<TrainingUsers> findByStatus(boolean status);
    public Optional<TrainingUsers> getTrainId(Long id);
    Object[] getStudentsWithBatch(Long studentId);

	public Optional<BatchAndFees> getbatchAndFees(Long id);
}
