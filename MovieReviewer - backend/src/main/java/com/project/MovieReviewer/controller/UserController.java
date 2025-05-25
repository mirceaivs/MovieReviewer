package com.project.MovieReviewer.controller;

import com.project.MovieReviewer.model.dto.ReviewDTO;
import com.project.MovieReviewer.model.dto.UserFullProfileDTO;
import com.project.MovieReviewer.service.ReviewService;
import com.project.MovieReviewer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/me")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ReviewService reviewService;

    @GetMapping
    public ResponseEntity<UserFullProfileDTO> getCurrentUserProfile(@AuthenticationPrincipal UserDetails user) {
        UserFullProfileDTO dto = userService.getCurrentUserProfile(user.getUsername());
        return ResponseEntity.ok(dto);
    }

    @PutMapping
    public ResponseEntity<Void> updateUser(@AuthenticationPrincipal UserDetails user,
                                           @RequestBody UserFullProfileDTO dto) {
        userService.updateUser(user.getUsername(), dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal UserDetails user) {
        userService.deleteUser(user.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewDTO>> getMyReviews(@AuthenticationPrincipal UserDetails userDetails) {
        List<ReviewDTO> list = reviewService.getReviewsByUser(userDetails.getUsername());
        return ResponseEntity.ok(list);
    }
}
