package com.project.MovieReviewer.model.dto.mapper;

import com.project.MovieReviewer.model.Actor;
import com.project.MovieReviewer.model.Genre;
import com.project.MovieReviewer.model.Movie;
import com.project.MovieReviewer.model.Rating;
import com.project.MovieReviewer.model.dto.MovieDTO;
import com.project.MovieReviewer.model.dto.response.OmdbResponse;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.project.MovieReviewer.utility.MovieUtils.*;


public class MovieMapper {

    // Din OmdbResponse (de la API) → Movie (entity)
    public static Movie fromOmdbResponseToEntity(OmdbResponse r, Function<String, Genre> genreResolver, Function<String, Actor> actorResolver) {
        Set<Genre> genres = r.getGenre() == null
                ? Collections.emptySet()
                : Arrays.stream(r.getGenre().split(",\\s*"))
                .map(genreResolver)
                .collect(Collectors.toSet());

        Set<Actor> actors = Arrays.stream(r.getActors().split(",\\s*"))
                .map(actorResolver)
                .collect(Collectors.toSet());

        return Movie.builder()
                .externalId(r.getImdbID())
                .title(r.getTitle())
                .year(r.getYear())
                .rated(r.getRated())
                .released(r.getReleased())
                .runtime(r.getRuntime())
                .genres(genres)
                .director(r.getDirector())
                .writer(r.getWriter())
                .actors(actors)
                .plot(r.getPlot())
                .language(r.getLanguage())
                .country(r.getCountry())
                .awards(r.getAwards())
                .poster(r.getPoster())
                .ratings(
                        r.getRatings() == null ? new ArrayList<>() :
                                r.getRatings().stream()
                                        .map(e -> Rating.builder()
                                                .source(e.getSource())
                                                .value(e.getValue())
                                                .build())
                                        .collect(Collectors.toList())
                )
                .metascore(parseIntOrNull(r.getMetascore()))
                .imdbRating(parseDoubleOrNull(r.getImdbRating()))
                .imdbVotes(parseLongOrNull(r.getImdbVotes()))
                .type(r.getType())
                .response(r.getResponse())
                .build();
    }

    // Din Movie (entity) → MovieDTO (ce trimitem spre front)
    public static MovieDTO fromEntityToDTO(Movie m) {
        return MovieDTO.builder()
                .id(m.getId())
                .externalId(m.getExternalId())
                .title(m.getTitle())
                .year(m.getYear())
                .rated(m.getRated())
                .released(m.getReleased())
                .runtime(m.getRuntime())
                .genres(m.getGenres().stream().map(Genre::getName).collect(Collectors.toSet()))
                .director(m.getDirector())
                .writer(m.getWriter())
                .actors(m.getActors().stream().map(Actor::getName).collect(Collectors.toSet()))
                .plot(m.getPlot())
                .language(m.getLanguage())
                .country(m.getCountry())
                .awards(m.getAwards())
                .poster(m.getPoster())
                .ratings(
                        m.getRatings() == null ? new ArrayList<>() :
                                m.getRatings().stream()
                                        .map(r -> MovieDTO.RatingDTO.builder()
                                                .source(r.getSource())
                                                .value(r.getValue())
                                                .build())
                                        .collect(Collectors.toList())
                )
                .metascore(m.getMetascore() != null ? m.getMetascore().toString() : null)
                .imdbRating(m.getImdbRating() != null ? String.valueOf(m.getImdbRating()) : null)
                .imdbVotes(m.getImdbVotes() != null ? String.valueOf(m.getImdbVotes()) : null)
                .type(m.getType())
                .response(m.getResponse())
                .build();


    }

    public static Movie fromDtoToEntity(
            MovieDTO dto,
            Function<String, Genre> genreResolver,
            Function<String, Actor> actorResolver // Adaugă resolver pentru actori!
    ) {
        Set<Genre> genres = dto.getGenres() == null
                ? Collections.emptySet()
                : dto.getGenres().stream()
                .map(genreResolver)
                .collect(Collectors.toSet());

        Set<Actor> actors = dto.getActors() == null
                ? Collections.emptySet()
                : dto.getActors().stream()
                .map(actorResolver)
                .collect(Collectors.toSet());

        List<Rating> ratings = dto.getRatings() == null
                ? new ArrayList<>()
                : dto.getRatings().stream()
                .map(r -> Rating.builder()
                        .source(r.getSource())
                        .value(r.getValue())
                        .build())
                .collect(Collectors.toList());

        return Movie.builder()
                .id(dto.getId())
                .externalId(dto.getExternalId())
                .title(dto.getTitle())
                .year(dto.getYear())
                .rated(dto.getRated())
                .released(dto.getReleased())
                .runtime(dto.getRuntime())
                .genres(genres)
                .director(dto.getDirector())
                .writer(dto.getWriter())
                .actors(actors) // <- Aici, nu mai e string!
                .plot(dto.getPlot())
                .language(dto.getLanguage())
                .country(dto.getCountry())
                .awards(dto.getAwards())
                .poster(dto.getPoster())
                .ratings(ratings)
                .metascore(parseIntOrNull(dto.getMetascore()))
                .imdbRating(parseDoubleOrNull(dto.getImdbRating()))
                .imdbVotes(parseLongOrNull(dto.getImdbVotes()))
                .type(dto.getType())
                .response(dto.getResponse())
                .build();
    }


}