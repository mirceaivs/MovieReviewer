package com.project.MovieReviewer.model.dto.mapper;

import com.project.MovieReviewer.model.Movie;
import com.project.MovieReviewer.model.Review;
import com.project.MovieReviewer.model.User;
import com.project.MovieReviewer.model.dto.ReviewDTO;
import com.project.MovieReviewer.repository.MovieRepository;
import com.project.MovieReviewer.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public static ReviewDTO toDTO(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .content(review.getContent())
                .rating(review.getRating())
                .username(review.getUser().getEmail())
                .movieId(review.getMovie().getId())
                .build();
    }

    public static Review toEntity(ReviewDTO dto, User user, Movie movie) {
        return Review.builder()
                .id(dto.getId())
                .content(dto.getContent())
                .rating(dto.getRating())
                .user(user)
                .movie(movie)
                .build();
    }
}
