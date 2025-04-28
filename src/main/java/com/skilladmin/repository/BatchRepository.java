package com.skilladmin.repository;

import com.skilladmin.model.Batch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BatchRepository extends JpaRepository<Batch,Long> {

    @Query(value = "select tb.* from traing_batch tb where tb.tutors_id=:tutorId and tb.batch_id=:batchId",nativeQuery = true)
    public  Batch findByTutorIdAndBatchId(Long tutorId,Long batchId);

}
