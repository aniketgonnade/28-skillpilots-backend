package com.skilladmin.service;

import com.skilladmin.model.FeedbackData;

public interface FeedbacklService {
	
	
	public FeedbackData saveFeedback(FeedbackData feedbackData,Long internshipId,Long studentId);
	public FeedbackData getFeedbackData(Long internship_id,Long feedback_from) ;

}
