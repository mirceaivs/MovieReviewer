package com.project.MovieReviewer.service;

import com.project.MovieReviewer.model.dto.WatchlistDTO;

import java.util.List;

public interface WatchlistService {
    List<WatchlistDTO> getByUser(String username);
    WatchlistDTO add(Long movieId, String username);
    void remove(Long movieId, String username);
}
