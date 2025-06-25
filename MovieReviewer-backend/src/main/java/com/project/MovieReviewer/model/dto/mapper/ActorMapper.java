package com.project.MovieReviewer.model.dto.mapper;

import com.project.MovieReviewer.model.Actor;
import com.project.MovieReviewer.model.dto.ActorDTO;

public class ActorMapper {
    public static ActorDTO toDTO(Actor actor) {
        if (actor == null) return null;
        return ActorDTO.builder()
                .id(actor.getId())
                .name(actor.getName())
                .build();
    }

    public static Actor toEntity(ActorDTO dto) {
        if (dto == null) return null;
        return Actor.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }
}
