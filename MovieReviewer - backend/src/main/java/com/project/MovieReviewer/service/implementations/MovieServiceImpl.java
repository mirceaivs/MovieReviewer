package com.project.MovieReviewer.service.implementations;
import com.project.MovieReviewer.model.*;
import com.project.MovieReviewer.model.dto.MovieDTO;
import com.project.MovieReviewer.model.dto.response.OmdbResponse;
import com.project.MovieReviewer.model.dto.mapper.MovieMapper;
import com.project.MovieReviewer.model.dto.response.OmdbSearchResponse;
import com.project.MovieReviewer.repository.ActorRepository;
import com.project.MovieReviewer.repository.GenreRepository;
import com.project.MovieReviewer.repository.MovieRepository;
import com.project.MovieReviewer.service.MovieService;
import com.project.MovieReviewer.utility.ExternalMovieClient;
import com.project.MovieReviewer.utility.MovieSpecifications;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final ExternalMovieClient externalMovieClient;
    private final GenreRepository genreRepository;
    private final ActorRepository actorRepository;

    @Override
    public MovieDTO getById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found: " + id));
        return MovieMapper.fromEntityToDTO(movie);
    }

    @Override
    public Page<MovieDTO> getAllMovies(Pageable pageable) {
        Page<Movie> page = movieRepository.findAll(pageable);
        return page.map(MovieMapper::fromEntityToDTO);
    }

    @Override
    public Page<MovieDTO> searchByTitle(String title, Pageable pageable) {
        log.info("Searching movies by title: {} [page: {}, size: {}]", title, pageable.getPageNumber(), pageable.getPageSize());
        Page<Movie> page = movieRepository.findByTitleContainingIgnoreCase(title, pageable);
        return page.map(MovieMapper::fromEntityToDTO);
    }

    @Override
    public Page<MovieDTO> searchMovies(String title, Set<String> genres, Set<String> actors, Pageable pageable) {
        Specification<Movie> spec = Specification.where(null);

        boolean onlyTitle = (title != null && !title.isEmpty())
                && (genres == null || genres.isEmpty())
                && (actors == null || actors.isEmpty());

        if (title != null && !title.isEmpty()) {
            spec = spec.and(MovieSpecifications.hasTitle(title));
        }

        if (genres != null && !genres.isEmpty()) {
            spec = spec.and(MovieSpecifications.hasGenres(genres));
        }

        if (actors != null && !actors.isEmpty()) {
            spec = spec.and(MovieSpecifications.hasActors(actors));
        }

        Page<Movie> page = movieRepository.findAll(spec, pageable);

        if (onlyTitle && !page.hasContent()) {
            log.info("Not found locally. Fetching from OMDb...");
            OmdbResponse r = externalMovieClient.fetchMovieByTitle(title);
            if (r != null && "True".equalsIgnoreCase(r.getResponse())) {
                Movie movie = MovieMapper.fromOmdbResponseToEntity(r, this::genreResolver, this::actorResolver);
                movieRepository.save(movie);
                return movieRepository.findAll(spec, pageable).map(MovieMapper::fromEntityToDTO);
            }
        }

        return page.map(MovieMapper::fromEntityToDTO);
    }

    @Override
    public MovieDTO save(MovieDTO dto) {
        Movie movie = MovieMapper.fromDtoToEntity(dto, this::genreResolver, this::actorResolver);
        movie = movieRepository.save(movie);
        return MovieMapper.fromEntityToDTO(movie);
    }

    @Override
    public void delete(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new EntityNotFoundException("Movie not found: " + id);
        }
        movieRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateMovie(Long id, MovieDTO dto) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found: " + id));

        boolean modified = false;

        if (dto.getTitle() != null && !dto.getTitle().isBlank() && !dto.getTitle().equals(movie.getTitle())) {
            movie.setTitle(dto.getTitle());
            modified = true;
        }
        if (dto.getYear() != null && !dto.getYear().isBlank() && !dto.getYear().equals(movie.getYear())) {
            movie.setYear(dto.getYear());
            modified = true;
        }
        if (dto.getPlot() != null && !dto.getPlot().equals(movie.getPlot())) {
            movie.setPlot(dto.getPlot());
            modified = true;
        }
        if (dto.getGenres() != null && !dto.getGenres().isEmpty()) {
            Set<Genre> genres = dto.getGenres().stream().map(this::genreResolver).collect(Collectors.toSet());
            if (!genres.equals(movie.getGenres())) {
                movie.setGenres(genres);
                modified = true;
            }
        }

        if (modified) {
            movieRepository.save(movie);
        }
    }


    @Override
    public OmdbSearchResponse searchOmdb(String query, int page) {
        log.info("Searching OMDb for query='{}', page={}", query, page);
        return externalMovieClient.searchOmdb(query, page);
    }

    @Override
    public MovieDTO getByImdbId(String imdbId) {
        Optional<Movie> movieOpt = movieRepository.findByExternalId(imdbId);
        if (movieOpt.isPresent()) {
            return MovieMapper.fromEntityToDTO(movieOpt.get());
        }
        OmdbResponse r = externalMovieClient.fetchMovieByImdbId(imdbId);
        if (r != null && "True".equalsIgnoreCase(r.getResponse())) {
            Movie movie = MovieMapper.fromOmdbResponseToEntity(r, this::genreResolver, this::actorResolver);
            movie = movieRepository.save(movie);
            return MovieMapper.fromEntityToDTO(movie);
        }
        throw new EntityNotFoundException("Movie not found in OMDb or locally: " + imdbId);
    }

    private Genre genreResolver(String name) {
        return genreRepository.findByName(name)
                .orElseGet(() -> genreRepository.save(Genre.builder().name(name).build()));
    }

    private Actor actorResolver(String name) {
        return actorRepository.findByName(name)
                .orElseGet(() -> actorRepository.save(Actor.builder().name(name).build()));
    }
}