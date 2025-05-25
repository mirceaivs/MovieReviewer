package com.project.MovieReviewer.service.implementations;

import com.project.MovieReviewer.model.Movie;
import com.project.MovieReviewer.model.Review;
import com.project.MovieReviewer.model.User;
import com.project.MovieReviewer.model.dto.ReviewDTO;
import com.project.MovieReviewer.model.dto.mapper.ReviewMapper;
import com.project.MovieReviewer.repository.MovieRepository;
import com.project.MovieReviewer.repository.ReviewRepository;
import com.project.MovieReviewer.repository.UserRepository;
import com.project.MovieReviewer.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final Logger log = LoggerFactory.getLogger(ReviewServiceImpl.class);

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             MovieRepository movieRepository,
                             UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getReviewsByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        List<Review> reviews = reviewRepository.findByUser(user);
        return reviews.stream()
                .map(review -> {
                    ReviewDTO dto = ReviewMapper.toDTO(review);
                    dto.setMovieTitle(review.getMovie().getTitle());
                    return dto;
                })
                .collect(Collectors.toList());
    }



    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getByMovie(Long movieId) {
        log.info("Fetching reviews for movie {}", movieId);
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found: " + movieId));
        return movie.getReviews().stream()
                .map(ReviewMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    public ReviewDTO create(ReviewDTO dto) {
        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new EntityNotFoundException("Movie not found: " + dto.getMovieId()));
        User user = userRepository.findByEmail(dto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + dto.getUsername()));

        Optional<Review> existing = reviewRepository.findByUserAndMovie(user, movie);
        if (existing.isPresent()) {
            throw new IllegalStateException("You already reviewed this movie!");
        }

        Review review = ReviewMapper.toEntity(dto, user, movie);
        Review saved = reviewRepository.save(review);
        return ReviewMapper.toDTO(saved);
    }


    @Override
    public ReviewDTO update(Long id, ReviewDTO dto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found: " + id));
        review.setContent(dto.getContent());
        review.setRating(dto.getRating());
        Review updated = reviewRepository.save(review);
        return ReviewMapper.toDTO(updated);
    }


    @Override
    public void delete(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new EntityNotFoundException("Review not found: " + id);
        }
        reviewRepository.deleteById(id);
        log.info("Deleted review {}", id);
    }
}