package org.example.group2siouxbackend.persistence.interfaces.shelves;

import org.example.group2siouxbackend.persistence.entity.shelf.RoomEntity;
import org.example.group2siouxbackend.persistence.entity.shelf.ShelfEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShelfRepository extends JpaRepository<ShelfEntity, Integer> {

    @Query("""
    SELECT COUNT(p)
    FROM PackageEntity p
    WHERE p.locationEntity.room = :room
      AND p.locationEntity.shelf = :shelf
""")
    Long isShelfOccupied(Integer room, String shelf);

    Optional<ShelfEntity> findByIdAndRoom_Number(int shelfId, int roomNumber);

    Integer room(RoomEntity room);
}
