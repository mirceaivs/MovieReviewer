package com.project.MovieReviewer.model.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


//@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
//public class MovieDTO {
//    private Long id;
//    private String title;
//    private int releaseYear;
//    private double averageRating;
//    private String description;
//    @Builder.Default
//    private Set<String> genres = new HashSet<>();
//
//}

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class MovieDTO {
    private Long id;
    private String externalId;
    private String title;
    private String year;
    private String rated;
    private String released;
    private String runtime;
    @Builder.Default
    private Set<String> genres = new HashSet<>();
    private String director;
    private String writer;
    @Builder.Default
    private Set<String> actors = new HashSet<>();
    private String plot;
    private String language;
    private String country;
    private String awards;
    private String poster;
    @Builder.Default
    private List<RatingDTO> ratings = new ArrayList<>();
    private String metascore;
    private String imdbRating;
    private String imdbVotes;
    private String type;
    private String response;

    public void setActors(Object value) {
        if (value instanceof String s && !s.isEmpty()) {
            this.actors = Set.of(s.split(",\\s*"));
        } else if (value instanceof Set<?> set) {
            this.actors = (Set<String>) set;
        }
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class RatingDTO {
        private String source;
        private String value;
    }
}
