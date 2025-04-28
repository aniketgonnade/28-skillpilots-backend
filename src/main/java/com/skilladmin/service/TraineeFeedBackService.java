package com.skilladmin.service;

import com.skilladmin.model.TraineeFeedback;

import java.util.List;

public interface TraineeFeedBackService {

    public TraineeFeedback giveFeedBack(Long studentId,TraineeFeedback traineeFeedback);

    public List<TraineeFeedback> getStudentFeedback(Long studentId);

}
