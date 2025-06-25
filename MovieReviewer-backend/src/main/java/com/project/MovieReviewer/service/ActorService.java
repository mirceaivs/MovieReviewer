package com.project.MovieReviewer.service;

import com.project.MovieReviewer.model.dto.ActorDTO;

import java.util.List;

public interface ActorService {
    List<ActorDTO> findAll();
    ActorDTO findById(Long id);
    ActorDTO create(ActorDTO dto);
    ActorDTO update(Long id, ActorDTO dto);
    void delete(Long id);
}
