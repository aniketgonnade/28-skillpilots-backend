package com.skilladmin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skilladmin.model.Rating;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query(value = "SELECT u.username, r.* FROM rating r JOIN user u ON r.user_id = u.id ORDER BY r.id DESC LIMIT 5", nativeQuery = true)
    List<Object[]> findTop5ByOrderByIdDescNative();



}
