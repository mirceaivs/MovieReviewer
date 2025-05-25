package com.project.MovieReviewer.controller;

import com.project.MovieReviewer.model.Genre;
import com.project.MovieReviewer.model.dto.GenreDTO;
import com.project.MovieReviewer.model.dto.mapper.GenreMapper;
import com.project.MovieReviewer.service.GenreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public ResponseEntity<List<GenreDTO>> getAllGenres() {
        List<GenreDTO> list = genreService.listAll()
                .stream()
                .map(g -> GenreDTO.builder()
                        .id(g.getId())
                        .name(g.getName())
                        .build())
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getGenre(@PathVariable Long id) {
        Genre dto = genreService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenreDTO> createGenre(@RequestBody @Valid GenreDTO dto) {
        Genre created = genreService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(GenreMapper.toDTO(created));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenreDTO> updateGenre(@PathVariable Long id, @RequestBody GenreDTO dto) {
        Genre updated = genreService.update(id, dto);
        return ResponseEntity.ok(GenreMapper.toDTO(updated));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        genreService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
