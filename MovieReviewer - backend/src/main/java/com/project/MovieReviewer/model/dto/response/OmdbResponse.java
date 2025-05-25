package com.project.MovieReviewer.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;


@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OmdbResponse {
    @JsonProperty("Title")     private String title;
    @JsonProperty("Year")      private String year;
    @JsonProperty("Rated")     private String rated;
    @JsonProperty("Released")  private String released;
    @JsonProperty("Runtime")   private String runtime;
    @JsonProperty("Genre")     private String genre;
    @JsonProperty("Director")  private String director;
    @JsonProperty("Writer")    private String writer;
    @JsonProperty("Actors")    private String actors;
    @JsonProperty("Plot")      private String plot;
    @JsonProperty("Language")  private String language;
    @JsonProperty("Country")   private String country;
    @JsonProperty("Awards")    private String awards;
    @JsonProperty("Poster")    private String poster;
    @JsonProperty("Ratings")   private List<RatingEntry> ratings;
    @JsonProperty("Metascore") private String metascore;
    @JsonProperty("imdbRating") private String imdbRating;
    @JsonProperty("imdbVotes")  private String imdbVotes;
    @JsonProperty("imdbID")     private String imdbID;
    @JsonProperty("Type")       private String type;
    @JsonProperty("Response")   private String response;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class RatingEntry {
        @JsonProperty("Source") private String source;
        @JsonProperty("Value")  private String value;
    }
}

