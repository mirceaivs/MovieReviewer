package com.project.MovieReviewer.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor @Builder
public class GenreDTO {
    private Long id;

    @NotBlank(message = "A genre must have a name!")
    private String name;
}
