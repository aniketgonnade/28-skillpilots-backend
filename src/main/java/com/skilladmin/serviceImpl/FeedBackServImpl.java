package com.skilladmin.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skilladmin.model.FeedbackData;
import com.skilladmin.model.Internships;
import com.skilladmin.repository.FeedbackRepository;
import com.skilladmin.repository.InternshipRepository;
import com.skilladmin.service.FeedbacklService;

@Service
public class FeedBackServImpl implements FeedbacklService {

	@Autowired
	private FeedbackRepository feedbackRepository;
	@Autowired
	private InternshipRepository internshipRepository;
	
	@Override
	public FeedbackData saveFeedback(FeedbackData feedbackData,Long internshipId,Long studentId) {
		Internships internships = internshipRepository.findById(internshipId).get();
		feedbackData.setFeedback_from(studentId);
		feedbackData.setFeedback_for(internships.getCompany_id());
		feedbackData.setInternship_id(internshipId);
		return feedbackRepository.save(feedbackData);
	}

	@Override
	public FeedbackData getFeedbackData(Long internship_id, Long feedback_from) {
		return feedbackRepository.getFeedbackData(internship_id, feedback_from);
	}

}
