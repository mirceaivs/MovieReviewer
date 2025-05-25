package com.project.MovieReviewer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "genre")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Please provide a genre!")
    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
    @JsonIgnore
    @Builder.Default
    private Set<Movie> movies = new HashSet<>();
}
