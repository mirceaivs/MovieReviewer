package com.project.MovieReviewer.service;

import com.project.MovieReviewer.model.Review;
import com.project.MovieReviewer.model.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {
    List<ReviewDTO> getByMovie(Long movieId);
    //ReviewDTO create(Long movieId, String username, ReviewDTO dto);
    ReviewDTO create(ReviewDTO dto);
    ReviewDTO update(Long id, ReviewDTO dto);
    void delete(Long id);
    List<ReviewDTO> getReviewsByUser(String email);

}