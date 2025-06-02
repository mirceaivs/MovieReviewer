package com.project.MovieReviewer.service.implementations;

import com.project.MovieReviewer.model.Movie;
import com.project.MovieReviewer.model.User;
import com.project.MovieReviewer.model.Watchlist;
import com.project.MovieReviewer.model.dto.WatchlistDTO;
import com.project.MovieReviewer.model.dto.mapper.WatchlistMapper;
import com.project.MovieReviewer.repository.MovieRepository;
import com.project.MovieReviewer.repository.UserRepository;
import com.project.MovieReviewer.repository.WatchlistRepository;
import com.project.MovieReviewer.service.WatchlistService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class WatchlistServiceImpl implements WatchlistService {
    private final WatchlistRepository watchlistRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final Logger log = LoggerFactory.getLogger(WatchlistServiceImpl.class);

    @Autowired
    public WatchlistServiceImpl(WatchlistRepository watchlistRepo,
                                MovieRepository movieRepository,
                                UserRepository userRepository) {
        this.watchlistRepository = watchlistRepo;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<WatchlistDTO> getByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("The user cannot be found!"));
        return watchlistRepository.findByUser(user).stream()
                .map(WatchlistMapper::toDTO)
                .collect(Collectors.toList());
    }



    @Override
    public WatchlistDTO add(Long movieId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("The user cannot be found!"));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found: " + movieId));

        boolean alreadyExists = watchlistRepository.findByUserAndMovie(user, movie).isPresent();
        if (alreadyExists) {
            throw new IllegalStateException("Movie is already in the watchlist!");
        }

        Watchlist watchlist = WatchlistMapper.toEntity(new WatchlistDTO(null, movieId, null), user, movie);
        watchlist = watchlistRepository.save(watchlist);
        return WatchlistMapper.toDTO(watchlist);
    }


    @Override
    public void remove(Long movieId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("The user cannot be found!"));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found: " + movieId));
        Watchlist entry = watchlistRepository.findByUserAndMovie(user, movie)
                .orElseThrow(() -> new EntityNotFoundException("Watchlist entry not found!"));
        watchlistRepository.delete(entry);
    }
}