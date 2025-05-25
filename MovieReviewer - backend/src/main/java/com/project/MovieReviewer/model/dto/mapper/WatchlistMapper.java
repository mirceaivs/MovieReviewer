package com.project.MovieReviewer.model.dto.mapper;

import com.project.MovieReviewer.model.Movie;
import com.project.MovieReviewer.model.User;
import com.project.MovieReviewer.model.Watchlist;
import com.project.MovieReviewer.model.dto.WatchlistDTO;
import com.project.MovieReviewer.repository.MovieRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

public class WatchlistMapper {

    public static WatchlistDTO toDTO(Watchlist watchlist) {
        return WatchlistDTO.builder()
                .id(watchlist.getId())
                .movieId(watchlist.getMovie() != null ? watchlist.getMovie().getId() : null)
                .dateAdded(watchlist.getDateAdded())
                .build();
    }

    public static Watchlist toEntity(WatchlistDTO dto, User user, Movie movie) {
        return Watchlist.builder()
                .id(dto.getId())
                .user(user)
                .movie(movie)
                .dateAdded(dto.getDateAdded() != null ? dto.getDateAdded() : LocalDate.now())
                .build();
    }
}

