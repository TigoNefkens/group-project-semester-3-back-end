package org.example.group2siouxbackend.persistence.interfaces.shelves;

import org.example.group2siouxbackend.model.Level;
import org.example.group2siouxbackend.persistence.entity.shelf.LevelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

@Repository
public interface LevelRepository extends JpaRepository<LevelEntity, Long> {
    Optional<LevelEntity> findById_ShelfIdAndId_Number(Integer shelfId, Integer number);

    List<LevelEntity> findById_ShelfId(Integer id);
}

