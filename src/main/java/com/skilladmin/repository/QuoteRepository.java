package com.skilladmin.repository;

import com.skilladmin.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepository extends JpaRepository<Quote,Long > {
}
