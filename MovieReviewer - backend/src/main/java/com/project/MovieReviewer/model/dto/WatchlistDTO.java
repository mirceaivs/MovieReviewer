package com.project.MovieReviewer.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;


@Setter @Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class WatchlistDTO {
    private Long id;

    @NotNull(message = "The movie must be provided!") // <-- Use NotNull for Long!
    private Long movieId;

    private LocalDate dateAdded;
}
