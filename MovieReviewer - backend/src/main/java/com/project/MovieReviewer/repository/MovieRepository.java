package com.project.MovieReviewer.repository;

import com.project.MovieReviewer.model.Movie;
import com.project.MovieReviewer.model.dto.MovieDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {
    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Optional<Movie> findByExternalId(String externalId);
}
