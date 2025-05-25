package com.project.MovieReviewer.controller;

import com.project.MovieReviewer.model.User;
import com.project.MovieReviewer.model.dto.ReviewDTO;
import com.project.MovieReviewer.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies/{movieId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getReviews(@PathVariable Long movieId) {
        List<ReviewDTO> list = reviewService.getByMovie(movieId);
        return ResponseEntity.ok(list);
    }


//    @PostMapping
//    public ResponseEntity<ReviewDTO> addReview(@PathVariable Long movieId,
//                                               @RequestBody ReviewDTO dto,
//                                               @AuthenticationPrincipal UserDetails userDetails) {
//        ReviewDTO created = reviewService.create(movieId, userDetails.getUsername(), dto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(created);
//    }

    @PostMapping
    public ResponseEntity<ReviewDTO> addReview(
            @PathVariable Long movieId,
            @RequestBody ReviewDTO dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // Suprascrii username cu cel din context, ca să NU poată clientul să pună ce vrea
        dto.setUsername(userDetails.getUsername());
        dto.setMovieId(movieId);
        ReviewDTO created = reviewService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Long movieId,
                                                  @PathVariable Long id,
                                                  @RequestBody ReviewDTO dto) {
        ReviewDTO updated = reviewService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long movieId,
                                             @PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
