package org.example.group2siouxbackend.business.converter.shelf;

import lombok.RequiredArgsConstructor;
import org.example.group2siouxbackend.dto.LevelDto;
import org.example.group2siouxbackend.model.Level;
import org.example.group2siouxbackend.persistence.entity.shelf.LevelEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
@Component
@RequiredArgsConstructor
public class LevelConverter {

    public static LevelDto EntityToDto(LevelEntity level) {
        return new LevelDto(
                (long)level.getId().getNumber(),
                level.getSections().stream().map(SectionConverter::EntityToDto).collect(Collectors.toList())
        );
    }
}
