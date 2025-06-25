package com.project.MovieReviewer.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter @AllArgsConstructor
public class RegisterDTO {
    @NotBlank(message = "Email should be provided!")
    private String email;

    @NotBlank(message = "Password must be completed!")
    private String password;

    @NotBlank(message = "First name cannot be blank!")
    private String firstName;

    @NotBlank(message = "Last name should be provided!")
    private String lastName;

    @NotBlank(message = "A username should be provided!")
    private String username;

}
