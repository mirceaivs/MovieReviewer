package com.project.MovieReviewer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;


@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Rating {
    private String source;
    @Column(name = "rating_value")
    private String value;
}
