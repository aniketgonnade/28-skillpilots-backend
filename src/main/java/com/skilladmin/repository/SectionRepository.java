package com.skilladmin.repository;

import com.skilladmin.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import com.skilladmin.model.Section;

import java.util.List;
import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section,  Long> {
    Optional<Section> findBySectionNameAndTest(String sectionName, Test test);

    List<Section> findByTest(Test test);
}
