package com.project.MovieReviewer.controller;

import com.project.MovieReviewer.model.dto.WatchlistDTO;
import com.project.MovieReviewer.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/watchlist")
public class WatchlistController {

    private final WatchlistService watchlistService;

    @Autowired
    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

//    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<List<WatchlistDTO>> getWatchlist(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<WatchlistDTO> list = watchlistService.getByUser(userDetails.getUsername());
        return ResponseEntity.ok(list);
    }


    //    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{movieId}")
    public ResponseEntity<WatchlistDTO> addToWatchlist(
            @PathVariable Long movieId,
            @AuthenticationPrincipal UserDetails userDetails) {
        WatchlistDTO created = watchlistService.add(movieId, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

//    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> removeFromWatchlist(
            @PathVariable Long movieId,
            @AuthenticationPrincipal UserDetails userDetails) {
        watchlistService.remove(movieId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
