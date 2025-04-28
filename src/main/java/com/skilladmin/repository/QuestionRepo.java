package com.skilladmin.repository;

import com.skilladmin.enumclass.QuestionLevel;
import com.skilladmin.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import com.skilladmin.model.Question;

import java.util.List;

public interface QuestionRepo extends JpaRepository<Question, Long> {
    List<Question> findBySection(Section section);

    List<Question> findBySectionIdAndQuestionLevel(Long sectionId, QuestionLevel level);

    List<Question> findBySectionId(Long id);
}
