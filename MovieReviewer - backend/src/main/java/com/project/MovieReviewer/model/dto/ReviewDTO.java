package com.project.MovieReviewer.model.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Setter @Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class ReviewDTO {
    private Long id;

    @NotBlank(message = "Content must not be blank")
    private String content;

    @Min(value = 1, message = "Rating must be between 1 and 5")
    @Max(value = 5, message = "Rating must be between 1 and 5")
    private int rating;

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotNull(message = "Movie ID cannot be null")
    private Long movieId;

    @Nullable
    private String movieTitle;
}
