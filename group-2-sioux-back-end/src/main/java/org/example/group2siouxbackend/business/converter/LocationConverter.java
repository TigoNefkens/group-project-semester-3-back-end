package org.example.group2siouxbackend.business.converter;

import lombok.RequiredArgsConstructor;
import org.example.group2siouxbackend.model.Location;
import org.example.group2siouxbackend.persistence.entity.LocationEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationConverter {
    public static LocationEntity fromDomainToEntity(Location domain){
        return LocationEntity.builder()
                .id(null)
                .room(Math.toIntExact(domain.getRoom()))
                .shelf(domain.getShelf())
                .level(domain.getLevel())
                .section(domain.getSection())
                .build();
    }
}
