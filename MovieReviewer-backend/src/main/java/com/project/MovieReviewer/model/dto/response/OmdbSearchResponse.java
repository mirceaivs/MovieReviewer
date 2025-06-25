package com.project.MovieReviewer.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.MovieReviewer.model.dto.OmdbShortMovieDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OmdbSearchResponse {
    @JsonProperty("Search")
    private List<OmdbShortMovieDTO> search;

    @JsonProperty("totalResults")
    private String totalResults;

    @JsonProperty("Response")
    private String response;
}
