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


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class MovieControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MovieRepository movieRepository;

    private String jwtToken;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void initUser() throws Exception {
        String registerJson = mapper.writeValueAsString(Map.of(
                "email", "movieuser@test.com",
                "password", "moviepass",
                "firstName", "Movie",
                "lastName", "Tester",
                "username", "movieuser"
        ));
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJson))
                .andExpect(status().isOk());

        String loginJson = mapper.writeValueAsString(Map.of(
                "email", "movieuser@test.com",
                "password", "moviepass"
        ));
        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn();
        Map<String,Object> resp = mapper.readValue(
                result.getResponse().getContentAsString(), Map.class);
        jwtToken = (String) resp.get("token");
    }

    @Test
    void getAllMovies_WithNoData_ShouldReturnEmptyPage() throws Exception {
        mockMvc.perform(get("/movies")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    void getMovieById_NotFound_ShouldReturnError() throws Exception {
        mockMvc.perform(get("/movies/999")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void createAndFetchMovie_AsUnauthorizedUser_ShouldBeForbidden() throws Exception {
        String movieJson = mapper.writeValueAsString(Map.of(
                "title", "New Movie", "year", "2023", "plot", "Description"
        ));
        mockMvc.perform(post("/movies")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(movieJson))
                .andExpect(status().isForbidden());
    }

    @Test
    void searchMovies_ShouldFilterByTitle() throws Exception {
        Movie movie = new Movie();
        movie.setTitle("UniqueTitle");
        movie.setYear("2023");
        movie.setCountry("Romania");
        movie.setAwards("None");
        movie.setResponse("True");
        movie.setLanguage("Romana");
        movie.setDirector("Boss Director");
        movie.setRated("PG");
        movie.setRuntime("120 min");
        movie.setReleased("2023-05-28");
        movie.setType("movie");
        movie.setWriter("Writer Boss");
        movie.setPlot("Un film genial");
        movie.setPoster("http://poster.com/film.jpg");
        movieRepository.save(movie);

        mockMvc.perform(get("/movies/search?title=UniqueTitle")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("UniqueTitle"));
    }


    @Test
    void omdbSearch_MissingQueryParam_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/movies/omdb-search")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isBadRequest());
    }
}
