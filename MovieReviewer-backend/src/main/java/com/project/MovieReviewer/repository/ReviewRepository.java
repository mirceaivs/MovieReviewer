package com.project.MovieReviewer.repository;

import com.project.MovieReviewer.model.Movie;
import com.project.MovieReviewer.model.Review;
import com.project.MovieReviewer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByUserAndMovie(User user, Movie movie);
    List<Review> findByUser(User user);
}
