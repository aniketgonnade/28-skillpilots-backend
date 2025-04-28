package com.skilladmin.serviceImpl;

import com.skilladmin.model.SessionFeedback;
import com.skilladmin.repository.SessionFeedbackRepo;
import com.skilladmin.service.SessionFeedbackService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionFeddbackServiceImpl implements SessionFeedbackService {

   private final SessionFeedbackRepo sessionFeedbackRepo;

    public SessionFeddbackServiceImpl(SessionFeedbackRepo sessionFeedbackRepo) {
        this.sessionFeedbackRepo = sessionFeedbackRepo;
    }

    @Override
    public SessionFeedback saveFeedback(SessionFeedback feedback) {
        return sessionFeedbackRepo.save(feedback);
    }

    @Override
    public List<SessionFeedback> getAllFeedBack() {
        return sessionFeedbackRepo.findAll();
    }
}
