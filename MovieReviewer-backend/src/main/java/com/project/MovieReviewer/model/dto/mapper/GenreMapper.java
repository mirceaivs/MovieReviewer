package com.project.MovieReviewer.model.dto.mapper;

import com.project.MovieReviewer.model.Genre;
import com.project.MovieReviewer.model.dto.GenreDTO;

public class GenreMapper {
    public static GenreDTO toDTO(Genre genre) {
        return GenreDTO.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }
    public static Genre fromDTO(GenreDTO dto) {
        return Genre.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }
}
