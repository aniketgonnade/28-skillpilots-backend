package com.skilladmin.repository;

import com.skilladmin.model.SessionFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionFeedbackRepo extends JpaRepository<SessionFeedback,Long> {
}
