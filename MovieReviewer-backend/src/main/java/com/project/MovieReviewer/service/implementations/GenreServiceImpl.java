package com.project.MovieReviewer.service.implementations;

import com.project.MovieReviewer.model.Genre;
import com.project.MovieReviewer.model.dto.GenreDTO;
import com.project.MovieReviewer.repository.GenreRepository;
import com.project.MovieReviewer.service.GenreService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private final Logger log = LoggerFactory.getLogger(GenreServiceImpl.class);

    @Autowired
    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Genre> listAll() {
        return genreRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Genre getById(Long id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found: " + id));
    }

    @Override
    public Genre create(GenreDTO genreDTO) {
        log.info("Creating genre {}", genreDTO.getName());
        Genre genre = Genre.builder()
                .name(genreDTO.getName())
                .build();
        return genreRepository.save(genre);

    }

    @Override
    public Genre update(Long id, GenreDTO dto) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found: " + id));
        genre.setName(dto.getName());
        return genreRepository.save(genre);
    }

    @Override
    public void delete(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found"));

        if (!genre.getMovies().isEmpty()) {
            throw new IllegalStateException("You can't delete a genre associated to a movie!");
        }
        genreRepository.delete(genre);
    }
}
