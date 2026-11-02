package org.example.group2siouxbackend.persistence.interfaces.shelves;

import org.example.group2siouxbackend.dto.RoomNameDto;
import org.example.group2siouxbackend.persistence.entity.shelf.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository  extends JpaRepository<RoomEntity, Integer> {
    @Query("select new org.example.group2siouxbackend.dto.RoomNameDto(r.id, r.name) from RoomEntity r order by r.number")
    List<RoomNameDto> findAllRoomNamesWithId();

    Optional<RoomEntity> findByNumber(int roomNumber);
}
