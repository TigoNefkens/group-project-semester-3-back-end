package org.example.group2siouxbackend.persistence.interfaces.shelves;

import org.example.group2siouxbackend.persistence.entity.shelf.LevelEntity;
import org.example.group2siouxbackend.persistence.entity.shelf.SectionEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SectionRepository extends CrudRepository<SectionEntity,Integer> {
    @Modifying
    @Query("""
UPDATE SectionEntity s
SET s.isFull = :isFull
WHERE s.id.levelNumber = :levelNumber
  AND s.id.sectionId = :sectionId
  AND s.id.shelfId = (
        SELECT sh.id
        FROM ShelfEntity sh
        WHERE sh.letter = :shelfName
    )
""")
    int updateIsFull(
            String shelfName,
            Integer levelNumber,
            Integer sectionId,
            boolean isFull
    );

    Optional<SectionEntity> findById_ShelfIdAndId_LevelNumberAndId_SectionId(Integer id_shelfId, Integer id_levelNumber, Integer id_sectionId);

    List<SectionEntity> findById_ShelfIdAndId_LevelNumber(Integer id, int levelNumber);

    void deleteById_ShelfIdAndId_LevelNumber(Integer id, Integer number);

    @Query("""
    SELECT CASE WHEN COUNT(s) > 0 THEN TRUE ELSE FALSE END
                                     FROM SectionEntity s
                                     JOIN ShelfEntity sh ON sh.id = s.id.shelfId
                                     WHERE sh.room.id = :room
                                       AND sh.letter = :shelf
                                       AND s.id.levelNumber = :level
                                       AND s.id.sectionId = :section
""")
    boolean existsSectionByLocation(int room, String shelf, int level, int section);

//    @Query("""
//    select s.isFull
//    from SectionEntity s
//    where s.id.levelNumber = :levelNumber
//      and s.id.sectionId = :sectionId
//""")
//    Optional<Boolean> findIsFull(
//            @Param("levelNumber") Integer levelNumber,
//            @Param("sectionId") Integer sectionId
//    );
}
