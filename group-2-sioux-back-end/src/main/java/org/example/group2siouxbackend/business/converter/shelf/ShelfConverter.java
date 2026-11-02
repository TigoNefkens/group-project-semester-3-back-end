package org.example.group2siouxbackend.business.converter.shelf;

import lombok.RequiredArgsConstructor;
import org.example.group2siouxbackend.dto.LevelDto;
import org.example.group2siouxbackend.dto.SectionDto;
import org.example.group2siouxbackend.dto.ShelfDto;
import org.example.group2siouxbackend.model.Level;
import org.example.group2siouxbackend.model.Section;
import org.example.group2siouxbackend.persistence.entity.shelf.ShelfEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
@Component
@RequiredArgsConstructor
public class ShelfConverter {

    public static ShelfDto EntityToDto(ShelfEntity shelf) {
        return new ShelfDto(
                (long)shelf.getId(),
                shelf.getLetter(),
                shelf.getColour(),
                shelf.getLevels().stream().map(LevelConverter::EntityToDto).collect(Collectors.toList())
        );
    }




}
