package com.project.MovieReviewer.service.implementations;

import com.project.MovieReviewer.model.Actor;
import com.project.MovieReviewer.model.dto.ActorDTO;
import com.project.MovieReviewer.model.dto.mapper.ActorMapper;
import com.project.MovieReviewer.repository.ActorRepository;
import com.project.MovieReviewer.service.ActorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActorServiceImpl implements ActorService {
    private final ActorRepository actorRepository;

    @Autowired
    public ActorServiceImpl(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @Override
    public List<ActorDTO> findAll() {
        return actorRepository.findAll()
                .stream()
                .map(ActorMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ActorDTO findById(Long id) {
        return actorRepository.findById(id)
                .map(ActorMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Actor not found: " + id));
    }

    @Override
    public ActorDTO create(ActorDTO dto) {
        if (actorRepository.findByName(dto.getName()).isPresent()) {
            throw new DataIntegrityViolationException("Actor already exists: " + dto.getName());
        }
        Actor saved = actorRepository.save(ActorMapper.toEntity(dto));
        return ActorMapper.toDTO(saved);
    }

    @Override
    public ActorDTO update(Long id, ActorDTO dto) {
        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Actor not found: " + id));
        actor.setName(dto.getName());
        Actor updated = actorRepository.save(actor);
        return ActorMapper.toDTO(updated);
    }

    @Override
    public void delete(Long id) {
        if (!actorRepository.existsById(id)) {
            throw new EntityNotFoundException("Actor not found: " + id);
        }
        actorRepository.deleteById(id);
    }
}
