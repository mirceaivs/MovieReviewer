package com.project.MovieReviewer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

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
class UserControllerIT {

    @Autowired private MockMvc mockMvc;
    private String jwtToken;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setupUser() throws Exception {
        // Register and login user
        String regJson = mapper.writeValueAsString(Map.of(
                "email", "profile@test.com", "password", "profilepass",
                "firstName", "Profile", "lastName", "User", "username", "profuser"
        ));
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(regJson)).andExpect(status().isOk());
        String loginJson = mapper.writeValueAsString(Map.of(
                "email", "profile@test.com", "password", "profilepass"
        ));
        MvcResult res = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginJson)).andReturn();
        jwtToken = mapper.readTree(res.getResponse().getContentAsString()).get("token").asText();
    }

    @Test
    void getUpdateAndDeleteUserProfile_Flow() throws Exception {
        // GET current profile
        mockMvc.perform(get("/users/me")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("profile@test.com"))
                .andExpect(jsonPath("$.firstName").value("Profile"));

        // Update profile (change firstName and lastName)
        String updateJson = mapper.writeValueAsString(Map.of(
                "firstName", "UpdatedName", "lastName", "UpdatedLast"
        ));
        mockMvc.perform(put("/users/me")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk());

        // Verify changes
        mockMvc.perform(get("/users/me")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("UpdatedName"))
                .andExpect(jsonPath("$.lastName").value("UpdatedLast"));

        // Delete the user
        mockMvc.perform(delete("/users/me")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNoContent());

        // Now any further GET should fail (user no longer exists)
        mockMvc.perform(get("/users/me")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getMyReviews_WhenNoneExist_ShouldReturnEmpty() throws Exception {
        mockMvc.perform(get("/users/me/reviews")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}

