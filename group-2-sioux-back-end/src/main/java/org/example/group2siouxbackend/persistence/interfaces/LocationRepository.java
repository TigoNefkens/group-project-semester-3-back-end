package org.example.group2siouxbackend.persistence.interfaces;

import org.example.group2siouxbackend.persistence.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Integer> {
    Optional<LocationEntity> findFirstByRoomAndShelfAndLevelAndSection(
            Integer room,
            String shelf,
            Integer level,
            Integer section
    );

    Optional<LocationEntity> findLocationEntityById(int id);
}
