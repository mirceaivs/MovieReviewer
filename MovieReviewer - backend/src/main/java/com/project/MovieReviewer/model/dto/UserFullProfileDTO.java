package com.project.MovieReviewer.model.dto;

import lombok.*;

import java.util.Date;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserFullProfileDTO {
    private Long id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String bio;
    private Date createdAt;
    private String password;
    private String oldPassword;
}
