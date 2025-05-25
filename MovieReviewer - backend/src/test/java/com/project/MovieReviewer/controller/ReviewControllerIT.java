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
class ReviewControllerIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private MovieRepository movieRepository;

    private String jwtToken;
    private ObjectMapper mapper = new ObjectMapper();
    private Long movieId;

    @BeforeEach
    void initData() throws Exception {
        // Register/login user
        String regJson = mapper.writeValueAsString(Map.of(
                "email", "reviewuser@test.com", "password", "reviewpass",
                "firstName", "Review", "lastName", "User", "username", "revuser"
        ));
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(regJson)).andExpect(status().isOk());
        String loginJson = mapper.writeValueAsString(Map.of(
                "email", "reviewuser@test.com", "password", "reviewpass"
        ));
        MvcResult res = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson)).andReturn();
        jwtToken = mapper.readTree(res.getResponse().getContentAsString()).get("token").asText();

        // Seed a movie for reviews
        Movie m = new Movie();
        m.setTitle("MovieForReview");
        movieId = movieRepository.save(m).getId();
    }

    @Test
    void getReviews_NoReviews_ShouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/movies/" + movieId + "/reviews")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void postReview_AndUpdateAndDelete_ShouldWork() throws Exception {
        // Create a new review for the movie
        String reviewJson = mapper.writeValueAsString(Map.of(
                "content", "Great movie!", "rating", 5
        ));
        MvcResult res = mockMvc.perform(post("/movies/" + movieId + "/reviews")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reviewJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
        Long reviewId = mapper.readTree(res.getResponse().getContentAsString()).get("id").asLong();

        // Update the review
        String updatedJson = mapper.writeValueAsString(Map.of(
                "content", "Actually, it was just good", "rating", 4
        ));
        mockMvc.perform(put("/movies/" + movieId + "/reviews/" + reviewId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Actually, it was just good"))
                .andExpect(jsonPath("$.rating").value(4));

        // Verify that GET reviews for user shows this review
        mockMvc.perform(get("/users/me/reviews")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Actually, it was just good"));

        // Delete the review
        mockMvc.perform(delete("/movies/" + movieId + "/reviews/" + reviewId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNoContent());

        // Now GET reviews for movie should be empty again
        mockMvc.perform(get("/movies/" + movieId + "/reviews")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void addDuplicateReview_ShouldReturnError() throws Exception {
        // Create first review
        String reviewJson = mapper.writeValueAsString(Map.of(
                "content", "Nice!", "rating", 4
        ));
        mockMvc.perform(post("/movies/" + movieId + "/reviews")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reviewJson))
                .andExpect(status().isCreated());

        // Attempt to create a second review for same movie by same user -> IllegalStateException
        mockMvc.perform(post("/movies/" + movieId + "/reviews")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reviewJson))
                .andExpect(status().is5xxServerError());
    }
}
