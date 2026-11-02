package org.example.group2siouxbackend.business.converter.shelf;

import lombok.RequiredArgsConstructor;
import org.example.group2siouxbackend.dto.SectionDto;
import org.example.group2siouxbackend.model.Section;
import org.example.group2siouxbackend.persistence.entity.shelf.SectionEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SectionConverter {

    public static SectionDto EntityToDto(SectionEntity section) {
        return new SectionDto(
                (long)section.getId().getSectionId(),
                section.getIsFull()
        );
    }
}
