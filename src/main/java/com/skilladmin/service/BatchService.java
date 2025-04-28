package com.skilladmin.service;

import com.skilladmin.model.Batch;
import com.skilladmin.model.Tutors;

import java.util.HashMap;
import java.util.List;

public interface BatchService {
    Batch createBatch(Batch batch);

    public HashMap<Object, Object> assignBatchToStudents(Long studentId, Long batchId, Long batchfees);

    public Tutors getTutorByBatchId(Long batchId);

    public List<Batch> getAllBatch();
    Batch getDataByBatchId(Long batchId);

}
