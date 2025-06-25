package com.project.MovieReviewer.utility;

import com.project.MovieReviewer.model.dto.response.OmdbResponse;
import com.project.MovieReviewer.model.dto.response.OmdbSearchResponse;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class ExternalMovieClient {
    private final RestTemplate restTemplate;
    @Value("${omdb.api.key}")
    private String apiKey;

    public OmdbResponse fetchMovieByTitle(String title) {
        String url = UriComponentsBuilder
                .fromHttpUrl("http://www.omdbapi.com/")
                .queryParam("apikey", apiKey)
                .queryParam("t", title)
                .queryParam("plot", "full")
                .build()
                .toUriString();

        OmdbResponse response = restTemplate.getForObject(url, OmdbResponse.class);
        if (response == null || "False".equalsIgnoreCase(response.getResponse())) {
            return null;
        }
        return response;
    }

    public OmdbSearchResponse searchOmdb(String query, int page) {
        String url = String.format("http://www.omdbapi.com/?s=%s&page=%d&apikey=%s",
                URLEncoder.encode(query, StandardCharsets.UTF_8), page, apiKey);
        return restTemplate.getForObject(url, OmdbSearchResponse.class);
    }

    public OmdbResponse fetchMovieByImdbId(String imdbId) {
        String url = UriComponentsBuilder
                .fromHttpUrl("http://www.omdbapi.com/")
                .queryParam("apikey", apiKey)
                .queryParam("i", imdbId)
                .queryParam("plot", "full")
                .build()
                .toUriString();

        OmdbResponse response = restTemplate.getForObject(url, OmdbResponse.class);
        if (response == null || "False".equalsIgnoreCase(response.getResponse())) {
            return null;
        }
        return response;
    }


}