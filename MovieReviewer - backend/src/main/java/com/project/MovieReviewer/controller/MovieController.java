package com.project.MovieReviewer.controller;

import com.project.MovieReviewer.model.dto.MovieDTO;
import com.project.MovieReviewer.model.dto.response.OmdbSearchResponse;
import com.project.MovieReviewer.service.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Set;


@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    // SEARCH by title, cu paginare: /api/movies/search?title=star&page=0&size=10
//    @GetMapping("/search")
//    public ResponseEntity<Page<MovieDTO>> searchMovies(
//            @RequestParam String title,
//            Pageable pageable) {
//        Page<MovieDTO> page = movieService.searchByTitle(title, pageable);
//        return ResponseEntity.ok(page);
//    }

    @GetMapping("/search")
    public ResponseEntity<Page<MovieDTO>> searchMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Set<String> genres,
            @RequestParam(required = false) Set<String> actors,
            Pageable pageable) {
        Page<MovieDTO> result = movieService.searchMovies(title, genres, actors, pageable);
        return ResponseEntity.ok(result);
    }

    // GET all movies (cu paginare, ex: /api/movies?page=0&size=20)
    @GetMapping("")
    public ResponseEntity<Page<MovieDTO>> getAllMovies(Pageable pageable) {
        Page<MovieDTO> page = movieService.getAllMovies(pageable);
        return ResponseEntity.ok(page);
    }

    // GET by ID: /api/movies/{id}
    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable Long id) {
        MovieDTO movie = movieService.getById(id);
        return ResponseEntity.ok(movie);
    }

    // CREATE movie (for admin/manual)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieDTO> createMovie(@Valid @RequestBody MovieDTO movieDTO) {
        MovieDTO created = movieService.save(movieDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateMovie(@PathVariable Long id, @RequestBody MovieDTO dto) {
        movieService.updateMovie(id, dto);
        return ResponseEntity.ok().build();
    }


    // DELETE movie (for admin/manual)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/omdb-search")
    public ResponseEntity<OmdbSearchResponse> searchOmdb(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(movieService.searchOmdb(query, page));
    }

    // Endpoint pentru detalii film (cu salvat in DB doar la selectare)
    @GetMapping("/by-imdb/{imdbId}")
    public ResponseEntity<MovieDTO> getMovieByImdb(@PathVariable String imdbId) {
        return ResponseEntity.ok(movieService.getByImdbId(imdbId));
    }
}
