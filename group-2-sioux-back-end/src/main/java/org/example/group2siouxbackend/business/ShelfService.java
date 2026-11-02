package org.example.group2siouxbackend.business;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.group2siouxbackend.business.converter.shelf.ShelfConverter;
import org.example.group2siouxbackend.dto.*;
import org.example.group2siouxbackend.model.*;
import org.example.group2siouxbackend.persistence.entity.shelf.*;
import org.example.group2siouxbackend.persistence.interfaces.LocationRepository;
import org.example.group2siouxbackend.persistence.interfaces.shelves.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ShelfService {

    private final ShelfRepository shelfRepository;
    private final RoomRepository roomRepository;
    private final LevelRepository levelRepository;
    private final SectionRepository sectionRepository;

    @Transactional
    public List<ShelfDto> getAllShelves() {
        return shelfRepository.findAll().stream().map(ShelfConverter::EntityToDto).collect(Collectors.toList());
    }

    @Transactional
    public List<ShelfDto> getShelvesByRoom(int roomNumber) {
        RoomEntity room = roomRepository
                .findByNumber(roomNumber)
                .orElseThrow(() -> new IllegalStateException("Room not found"));

        return room.getShelves()
                .stream()
                .map(ShelfConverter::EntityToDto)
                .toList();
    }


    private ShelfDto findShelf(int roomNumber, Long shelfId) {
        ShelfEntity shelf = shelfRepository
                .findByIdAndRoom_Number(
                        shelfId.intValue(), roomNumber
                )
                .orElse(null);

        return shelf != null ? ShelfConverter.EntityToDto(shelf) : null;
    }


    @Transactional
    public boolean editShelf(int roomNumber, Long shelfId, ShelfDto shelfDto) {
        long packagesLeft = shelfRepository.isShelfOccupied(roomNumber, shelfDto.getLetter());
    if(packagesLeft>0)
    {
        throw new IllegalStateException("Shelf has "+ packagesLeft +" packages left");
    }
        ShelfEntity shelf = shelfRepository
                .findByIdAndRoom_Number(shelfId.intValue(), roomNumber)
                .orElse(null);

        if (shelf == null) return false;

        shelf.setLetter(shelfDto.getLetter());
        shelf.setColour(shelfDto.getColour());

        shelf.getLevels().removeIf(level ->
                shelfDto.getLevels().stream()
                        .noneMatch(dto -> dto.getId().intValue() == level.getId().getNumber())
        );

        for (LevelDto levelDto : shelfDto.getLevels()) {

            int levelNumber = levelDto.getId().intValue();

            LevelEntity level = shelf.getLevels().stream()
                    .filter(l -> l.getId().getNumber() == levelNumber)
                    .findFirst()
                    .orElseGet(() -> {
                        LevelEntity newLevel = new LevelEntity();
                        newLevel.setId(new LevelEntity.LevelId(shelf.getId(), levelNumber));
                        newLevel.setShelf(shelf);
                        shelf.getLevels().add(newLevel);
                        return newLevel;
                    });


            level.getSections().removeIf(section ->
                    levelDto.getSections().stream()
                            .noneMatch(dto -> dto.getId().intValue() == section.getId().getSectionId())
            );

            for (SectionDto sectionDto : levelDto.getSections()) {

                int sectionNumber = sectionDto.getId().intValue();

                SectionEntity section = level.getSections().stream()
                        .filter(s -> s.getId().getSectionId() == sectionNumber)
                        .findFirst()
                        .orElseGet(() -> {
                            SectionEntity newSection = new SectionEntity();
                            SectionEntity.SectionId id = new SectionEntity.SectionId();
                            id.setShelfId(shelf.getId());
                            id.setLevelNumber(levelNumber);
                            id.setSectionId(sectionNumber);
                            newSection.setId(id);
                            newSection.setLevel(level);
                            level.getSections().add(newSection);
                            return newSection;
                        });

                section.setIsFull(sectionDto.getIsFull());
            }
        }


        shelfRepository.save(shelf);
        return true;
    }

    public boolean findShelfByLocation(int room, String shelf, int level, int section) {
        return sectionRepository.existsSectionByLocation(room, shelf, level, section);
    }

//    private Boolean findSectionStatus(Long levelId, Long sectionId) {
//    return sectionRepository.findIsFull(levelId.intValue(), sectionId.intValue())
//            .orElse(null);
//}

    @Transactional
    public List<RoomNameDto> getAllRoomNames() {
        return roomRepository.findAllRoomNamesWithId();
    }


    @Transactional
    public boolean updateSectionFullStatus(String shelfName, Integer levelNumber, Integer sectionId, boolean isFull)
    {
        int rowsUpdated = sectionRepository.updateIsFull(shelfName, levelNumber, sectionId, isFull);

        return rowsUpdated > 0;
    }


    @Transactional
    public void createShelf(int roomNumber, ShelfDto dto) {

        RoomEntity room = roomRepository
                .findByNumber(roomNumber)
                .orElseThrow(() -> new IllegalStateException("Room not found"));

        ShelfEntity shelf = new ShelfEntity();
        shelf.setLetter(dto.getLetter());
        shelf.setColour(dto.getColour());
        shelf.setRoom(room);

        shelfRepository.save(shelf);

        for (LevelDto levelDto : dto.getLevels()) {

            LevelEntity.LevelId levelId =
                    new LevelEntity.LevelId(
                            shelf.getId(),
                            levelDto.getId().intValue()
                    );

            LevelEntity level = new LevelEntity();
            level.setId(levelId);
            level.setShelf(shelf);

            levelRepository.save(level);

            for (SectionDto sectionDto : levelDto.getSections()) {

                SectionEntity section = new SectionEntity();

                SectionEntity.SectionId sectionId = new SectionEntity.SectionId();
                sectionId.setShelfId(shelf.getId());
                sectionId.setLevelNumber(levelId.getNumber());
                sectionId.setSectionId(sectionDto.getId().intValue());

                section.setId(sectionId);
                section.setLevel(level);
                section.setIsFull(sectionDto.getIsFull());

                sectionRepository.save(section);
            }
        }
    }
    @Transactional
    public boolean deleteShelf(int roomNumber, Long shelfId) {
        try {
            ShelfEntity shelf = shelfRepository
                    .findByIdAndRoom_Number(shelfId. intValue(), roomNumber)
                    .orElse(null);

            if (shelf == null) {
                return false;
            }

            long packagesLeft = shelfRepository.isShelfOccupied(roomNumber, shelf.getLetter());
            if (packagesLeft > 0) {
                throw new IllegalStateException("Cannot delete shelf:  it has " + packagesLeft + " packages left");
            }


            shelfRepository.delete(shelf);

            return true;

        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting shelf:  " + e.getMessage(), e);
        }
    }

    @Transactional
    public boolean createRoom(RoomDto roomDto) {
        if (roomRepository.findByNumber(roomDto.getNumber()).isPresent()) {
            return false;
        }

        RoomEntity room = new RoomEntity();
        room.setNumber(roomDto.getNumber());
        room.setName(roomDto.getName());

        roomRepository.save(room);

        return true;
    }
}


