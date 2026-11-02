package org.example.group2siouxbackend.business;

import lombok.AllArgsConstructor;
import org.example.group2siouxbackend.business.converter.LocationConverter;
import org.example.group2siouxbackend.model.Location;
import org.example.group2siouxbackend.persistence.entity.LocationEntity;
import org.example.group2siouxbackend.persistence.interfaces.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    public Long checkForLocation(Location location)
    {
        Optional<LocationEntity> retreatLocation = locationRepository.findFirstByRoomAndShelfAndLevelAndSection(Math.toIntExact(location.getRoom()), location.getShelf(), location.getLevel(), location.getSection());
        if(retreatLocation.isPresent())
        {
            return retreatLocation.get().getId().longValue();
        }
        return null;
    }

    public Long createLocation(Location location)
    {
        return locationRepository.save(LocationConverter.fromDomainToEntity(location)).getId().longValue();
    }
}