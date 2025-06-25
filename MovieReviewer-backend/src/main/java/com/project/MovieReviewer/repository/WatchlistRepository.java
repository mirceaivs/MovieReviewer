package com.project.MovieReviewer.repository;

import com.project.MovieReviewer.model.Movie;
import com.project.MovieReviewer.model.User;
import com.project.MovieReviewer.model.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    List<Watchlist> findByUser(User user);
    Optional<Watchlist> findByUserAndMovie(User user, Movie movie);
}
