package com.project.MovieReviewer.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class LoginDTO {
    @NotBlank(message = "Email should be completed!")
    private String email;

    @NotBlank(message = "Password should be completed!")
    private String password;
}
