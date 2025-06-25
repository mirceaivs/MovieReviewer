package com.project.MovieReviewer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "movies", uniqueConstraints = @UniqueConstraint(columnNames = "external_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", unique = true)
    private String externalId;

    @NotBlank(message = "Title must not be blank!")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Year must not be blank!")
    @Column(name = "release_year", nullable = false)
    private String year;

    @NotBlank(message = "Rated must not be blank!")
    private String rated;

    @ManyToMany
    @JoinTable(
            name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @Builder.Default
    private Set<Genre> genres = new HashSet<>();


    @NotBlank(message = "Released date must not be blank!")
    private String released;

    @NotBlank(message = "Runtime must not be blank!")
    private String runtime;

    @NotBlank(message = "Director must not be blank!")
    private String director;

    @NotBlank(message = "Writer must not be blank!")
    private String writer;

    @NotBlank(message = "Plot must not be blank!")
    @Column(columnDefinition = "TEXT")
    private String plot;

    @NotBlank(message = "Language must not be blank!")
    private String language;

    @NotBlank(message = "Country must not be blank!")
    private String country;

    @NotBlank(message = "Awards must not be blank!")
    private String awards;

    @NotBlank(message = "Poster URL must not be blank!")
    private String poster;

    @ElementCollection
    @CollectionTable(name = "movie_ratings", joinColumns = @JoinColumn(name = "movie_id"))
    @Builder.Default
    private List<Rating> ratings = new ArrayList<>();

    @Min(value = 0, message = "Metascore must be >= 0")
    @Max(value = 100, message = "Metascore must be <= 100")
    private Integer metascore;

    @DecimalMin(value = "0.0", message = "IMDb rating must be >= 0.0")
    @DecimalMax(value = "10.0", message = "IMDb rating must be <= 10.0")
    private Double imdbRating;

    @Min(value = 0, message = "IMDb votes must be >= 0")
    private Long imdbVotes;

    @NotBlank(message = "Type must not be blank!")
    private String type;

    @ManyToMany
    @JoinTable(
            name = "movie_actors",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    @Builder.Default
    private Set<Actor> actors = new HashSet<>();


    @NotBlank(message = "Response must not be blank!")
    private String response;

    @Builder.Default
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();
}