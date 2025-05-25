package com.project.MovieReviewer.service;
import com.project.MovieReviewer.model.dto.MovieDTO;
import java.util.List;
import java.util.Set;

import com.project.MovieReviewer.model.Movie;
import com.project.MovieReviewer.model.dto.response.OmdbSearchResponse;
import org.springframework.data.jpa.domain.Specification;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieService {
    MovieDTO getById(Long id);
    Page<MovieDTO> searchByTitle(String title, Pageable pageable);
//    Page<MovieDTO> searchMovies(Specification<Movie> spec, Pageable pageable);
    Page<MovieDTO> searchMovies(String title, Set<String> genres, Set<String> actors, Pageable pageable);
    MovieDTO save(MovieDTO dto);
    void delete(Long id);
    MovieDTO getByImdbId(String imdbId);
    OmdbSearchResponse searchOmdb(String query, int page);
    Page<MovieDTO> getAllMovies(Pageable pageable);
    void updateMovie(Long id, MovieDTO dto);
}
