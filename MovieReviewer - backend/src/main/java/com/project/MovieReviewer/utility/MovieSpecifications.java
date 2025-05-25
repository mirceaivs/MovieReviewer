package com.project.MovieReviewer.utility;

import com.project.MovieReviewer.model.Actor;
import com.project.MovieReviewer.model.Genre;
import com.project.MovieReviewer.model.Movie;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Set;

public class MovieSpecifications {


    public static Specification<Movie> hasTitle(String title) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Movie> hasGenres(Set<String> genres) {
        return (root, query, cb) -> {
            Join<Movie, Genre> genreJoin = root.join("genres");
            return genreJoin.get("name").in(genres);
        };
    }

    public static Specification<Movie> hasActors(Set<String> actors) {
        return (root, query, cb) -> {
            Join<Movie, Actor> actorJoin = root.join("actors");
            return actorJoin.get("name").in(actors);
        };
    }

}
