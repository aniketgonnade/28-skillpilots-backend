package com.skilladmin.repository;

import com.skilladmin.model.Batch;
import com.skilladmin.model.TrainingUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainingUserRepository extends JpaRepository<TrainingUsers,Long> {

    public  TrainingUsers findByEmail(String email);

    public  TrainingUsers findByStudentId(Long studentId);

    public List<TrainingUsers> findByBatchIdAndRoleAndStatus(Long batchId, String role,boolean status);

    @Query("select tu,tb.batchName from TrainingUsers tu join Batch tb on tu.batchId=tb.batchId where tu.role=:role and tu.status=true")
    public  List<Object[]> findByRoles(String role);

    @Query(value = "SELECT ta.status, tu.name FROM training_users tu RIGHT JOIN trainee_attendance ta ON " +
            "tu.user_id = ta.student_id WHERE ta.batch_id = :batchId AND ta.local_date_time = :localDate AND tu.role = :role",nativeQuery = true)
    List<Object[]> findStatusAndNameByBatchId(@Param("batchId") Long batchId,
                                              @Param("localDate") LocalDate localDate,
                                              @Param("role") String role);


    @Query("select tu.name,tb.batchName,tb.price from TrainingUsers tu join Batch tb on tu.batchId=tb.batchId where tu.role=:role")
    public List<Object[]>  findForFees();

    @Query("select tu.userId,tu.name from TrainingUsers tu where tu.role=:role and tu.status=true")
    List<Object[]> findByRole(String role);

    @Query(value ="select tu.* from training_users tu join traing_batch tb on tu.batch_id=tb.batch_id where tu.role=:role and tu.batch_id=:batchId",nativeQuery = true )
    List<TrainingUsers>  getStudents(String role,Long batchId);

    @Query(value = "select tu.* from training_users tu where tu.status=1",nativeQuery = true)
    List<TrainingUsers> getAll();

    public List<TrainingUsers> findByStatus(boolean status);

    @Query("select tu.address,tb.batchName,tu.name from TrainingUsers tu join Batch tb on tu.batchId=tb.batchId where tu.userId=:studentId")
    Object[] getStudentsWithBatch(Long studentId);

    TrainingUsers findByTutorId(Long tutorId);

	public TrainingUsers findByBatchfees(Long batchfees);
}
