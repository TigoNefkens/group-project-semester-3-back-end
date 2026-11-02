package org.example.group2siouxbackend.business;

import org.example.group2siouxbackend.model.Location;
import org.example.group2siouxbackend.persistence.entity.LocationEntity;
import org.example.group2siouxbackend.persistence.interfaces.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LocationServiceTest {

    //create fake repository
    @Mock
    private LocationRepository locationRepository;

    //create fake service
    @InjectMocks
    private LocationService locationService;

    @Test
    void checkForLocation_WhenExists_ShouldReturnId() {
        // create lcoation
        Location location = new Location(null, 101, "A", 2, 3);

        // create what the repository SHOULD return
        LocationEntity localEntity = LocationEntity.builder()
                .id(5)
                .room(101)
                .shelf("A")
                .level(2)
                .section(3)
                .build();

        when(locationRepository.findFirstByRoomAndShelfAndLevelAndSection(101, "A", 2, 3)).thenReturn(Optional.of(localEntity));

        // execute method
        Long result = locationService.checkForLocation(location);
        // verify result
        assertThat(result).isEqualTo(5L);
        // verify how many times we call the repository
        verify(locationRepository, times(1)).findFirstByRoomAndShelfAndLevelAndSection(101, "A", 2, 3);
    }

    @Test
    void checkForLocation_WhenNotExists_ShouldReturnNull() {
        Location location = new Location(null, 101, "A", 2, 3);

        when(locationRepository.findFirstByRoomAndShelfAndLevelAndSection(101, "A", 2, 3)).thenReturn(Optional.empty());

        Long result = locationService.checkForLocation(location);

        assertThat(result).isNull();
    }
}
