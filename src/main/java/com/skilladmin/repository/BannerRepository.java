package com.skilladmin.repository;

import com.skilladmin.model.Poster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Poster,Long> {
}
