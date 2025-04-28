package com.skilladmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.skilladmin.model.FeedbackData;

public interface FeedbackRepository extends JpaRepository<FeedbackData, Long> {
	@Query(value="select * from  feedback_data where internship_id=:internship_id AND feedback_from=:feedback_from",nativeQuery = true)
	public FeedbackData getFeedbackData(Long internship_id,Long feedback_from) ;
}
