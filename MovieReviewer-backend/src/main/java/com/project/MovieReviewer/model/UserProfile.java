package com.project.MovieReviewer.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "user_profile")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserProfile {

    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Username must not be blank!")
    private String username;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    @JsonBackReference
    private User user;

    @NotBlank(message = "First name can't be empty!")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Last name can't be empty!")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Nullable
    @Column(columnDefinition = "TEXT")
    @Size(max = 1000, message = "Bio must not exceed 1000 characters!")
    private String bio;

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private Date createdAt;



}
