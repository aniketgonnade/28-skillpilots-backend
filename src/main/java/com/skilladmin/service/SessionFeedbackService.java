package com.skilladmin.service;

import com.skilladmin.model.SessionFeedback;
import com.skilladmin.repository.SessionFeedbackRepo;

import java.util.List;

public interface SessionFeedbackService {


    public SessionFeedback saveFeedback(SessionFeedback feedback);

    public List<SessionFeedback> getAllFeedBack();

    }

