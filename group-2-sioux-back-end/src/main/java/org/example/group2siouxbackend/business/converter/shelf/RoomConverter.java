package org.example.group2siouxbackend.business.converter.shelf;

import lombok.RequiredArgsConstructor;
import org.example.group2siouxbackend.dto.RoomDto;
import org.example.group2siouxbackend.persistence.entity.shelf.RoomEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomConverter {
    public static RoomDto EntityToDto(RoomEntity entity) {
            return new RoomDto(
                    entity.getNumber(),
                    entity.getName(),
                    entity.getShelves()
                            .stream()
                            .map(ShelfConverter::EntityToDto)
                            .toList()
            );
    }
}

