package com.project.MovieReviewer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.project.MovieReviewer.model.dto.LoginDTO;
import com.project.MovieReviewer.model.dto.RegisterDTO;
import com.project.MovieReviewer.model.dto.response.LoginResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerAndLogin_flow_works() throws Exception {

        RegisterDTO reg = new RegisterDTO(
                "test@email.com",
                "SuperSecret1234",
                "Karlitos",
                "Student",
                "karlitosgenz"
        );

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reg)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@email.com"))
                .andExpect(jsonPath("$.userProfile.username").value("karlitosgenz"));

        LoginDTO login = new LoginDTO("test@email.com", "SuperSecret1234");

        String responseBody = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        LoginResponse loginResp = objectMapper.readValue(responseBody, LoginResponse.class);
        assertThat(loginResp.getToken()).isNotBlank();
        assertThat(loginResp.getExpiresIn()).isGreaterThan(0L);
    }

    @Test
    void register_fails_whenEmailAlreadyExists() throws Exception {
        RegisterDTO reg = new RegisterDTO(
                "alreadyused@email.com",
                "ParolaMea12345",
                "Karla",
                "Studenta",
                "karlauser"
        );
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reg)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reg)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void login_fails_withWrongPassword() throws Exception {
        RegisterDTO reg = new RegisterDTO(
                "wrongpass@email.com",
                "ParolaSuper12345",
                "Mario",
                "Popescu",
                "mariouser"
        );
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reg)))
                .andExpect(status().isOk());

        LoginDTO loginWrong = new LoginDTO("wrongpass@email.com", "gresita123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginWrong)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }
}
