package com.project.MovieReviewer.service;

import com.project.MovieReviewer.model.Genre;
import com.project.MovieReviewer.model.dto.GenreDTO;

import java.util.List;

public interface GenreService {
    List<Genre> listAll();
    Genre getById(Long id);
    Genre create(GenreDTO genreDTO);
    Genre update(Long id, GenreDTO dto);
    void delete(Long id);
}