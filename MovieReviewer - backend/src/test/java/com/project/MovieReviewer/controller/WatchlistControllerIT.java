package com.project.MovieReviewer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.MovieReviewer.model.Movie;
import com.project.MovieReviewer.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class WatchlistControllerIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private MovieRepository movieRepository;

    private String jwtToken;
    private ObjectMapper mapper = new ObjectMapper();
    private Long movieId;

    @BeforeEach
    void setup() throws Exception {
        // Register and login user
        String regJson = mapper.writeValueAsString(Map.of(
                "email", "watchlist@test.com", "password", "watchpass",
                "firstName", "Watch", "lastName", "Lister", "username", "watchuser"
        ));
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(regJson)).andExpect(status().isOk());
        String loginJson = mapper.writeValueAsString(Map.of(
                "email", "watchlist@test.com", "password", "watchpass"
        ));
        MvcResult res = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson)).andReturn();
        jwtToken = mapper.readTree(res.getResponse().getContentAsString()).get("token").asText();

        // Create a movie to add to watchlist
        Movie m = new Movie();
        m.setTitle("WatchMovie");
        movieId = movieRepository.save(m).getId();
    }

    @Test
    void emptyWatchlistInitially() throws Exception {
        mockMvc.perform(get("/watchlist")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void addAndRemoveFromWatchlist_Flow() throws Exception {
        // Add to watchlist
        mockMvc.perform(post("/watchlist/" + movieId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.movieId").value(movieId));

        // Now GET should contain one item
        mockMvc.perform(get("/watchlist")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].movieId").value(movieId));

        // Removing the movie
        mockMvc.perform(delete("/watchlist/" + movieId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNoContent());

        // Watchlist should be empty again
        mockMvc.perform(get("/watchlist")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void addDuplicateToWatchlist_ShouldFail() throws Exception {
        // First add
        mockMvc.perform(post("/watchlist/" + movieId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isCreated());

        // Add same movie again -> IllegalStateException
        mockMvc.perform(post("/watchlist/" + movieId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().is5xxServerError());
    }
}
