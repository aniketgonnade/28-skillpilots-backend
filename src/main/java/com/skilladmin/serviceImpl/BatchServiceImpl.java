package com.skilladmin.serviceImpl;

import com.skilladmin.model.Batch;
import com.skilladmin.model.TrainingUsers;
import com.skilladmin.model.Tutors;
import com.skilladmin.repository.BatchRepository;
import com.skilladmin.repository.TrainingUserRepository;
import com.skilladmin.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class BatchServiceImpl implements BatchService {

    @Autowired
    private BatchRepository batchRepository;
    @Autowired
    private TrainingUserRepository trainingUserRepository;
    @Override
    public Batch createBatch(Batch batch) {
        return batchRepository.save(batch);
    }


@Override
public HashMap<Object, Object> assignBatchToStudents(Long studentId, Long batchId, Long batchfees) {
    HashMap<Object, Object> response = new HashMap<>();

    try {
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch is not Found: " + batchId));

        TrainingUsers trainingUsers = trainingUserRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student is not Found: " + studentId));

        trainingUsers.setBatchId(batch.getBatchId());
        trainingUsers.setBatchfees(batchfees);
        trainingUserRepository.save(trainingUsers);
        response.put("msg", "Batch & fees assigned successfully.");
    } catch (RuntimeException e) {
        response.put("msg", e.getMessage()); // Propagate specific error messages
    } catch (Exception e) {
        response.put("msg", "An unexpected error occurred: " + e.getMessage());
    }

    return response;
}

    @Override
    public Tutors getTutorByBatchId(Long batchId) {
        Batch batch = batchRepository.findById(batchId).get();
        if (batch != null) {
            return batch.getTutors();
        } else {
            return null;
        }
    }

    @Override
    public List<Batch> getAllBatch() {
     return    batchRepository.findAll();
    }

    @Override
    public Batch getDataByBatchId(Long batchId) {
        return batchRepository.findById(batchId) .orElseThrow(() -> new RuntimeException("Batch not found for ID: " + batchId));
    }

}
