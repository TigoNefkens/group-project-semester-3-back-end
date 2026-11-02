package org.example.group2siouxbackend.business;

import org.example.group2siouxbackend.dto.ShelfDto;
import org.example.group2siouxbackend.persistence.entity.shelf.RoomEntity;
import org.example.group2siouxbackend.persistence.entity.shelf.ShelfEntity;
import org.example.group2siouxbackend.persistence.interfaces.LocationRepository;
import org.example.group2siouxbackend.persistence.interfaces.PackageRepository;
import org.example.group2siouxbackend.persistence.interfaces.shelves.LevelRepository;
import org.example.group2siouxbackend.persistence.interfaces.shelves.RoomRepository;
import org.example.group2siouxbackend.persistence.interfaces.shelves.SectionRepository;
import org.example.group2siouxbackend.persistence.interfaces.shelves.ShelfRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShelfServiceTest {
    @Mock
    private ShelfRepository shelfRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private LevelRepository levelRepository;

    @Mock
    private SectionRepository sectionRepository;

    @InjectMocks
    private ShelfService shelfService;

    @Test
    void findShelfByLocation_WhenExists_ShouldReturnTrue() {
        int roomNumber = 101;
        String shelfLetter = "A";
        int levelNumber = 2;
        int sectionNumber = 3;

        when(sectionRepository.existsSectionByLocation(roomNumber, shelfLetter, levelNumber, sectionNumber)).thenReturn(true);
        boolean result = shelfService.findShelfByLocation(roomNumber, shelfLetter, levelNumber, sectionNumber);
        assertThat(result).isTrue();
        verify(sectionRepository, times(1)).existsSectionByLocation(roomNumber, shelfLetter, levelNumber, sectionNumber);
    }

    @Test
    void findShelfByLocation_WhenNotExists_ShouldReturnFalse() {
        int roomNumber = 999;
        String shelfLetter = "Z";
        int levelNumber = 10;
        int sectionNumber = 10;

        when(sectionRepository.existsSectionByLocation(roomNumber, shelfLetter, levelNumber, sectionNumber)).thenReturn(false);
        boolean result = shelfService.findShelfByLocation(roomNumber, shelfLetter, levelNumber, sectionNumber);
        assertThat(result).isFalse();
    }

    @Test
    void getAllShelves_ShouldReturnAllShelves() {
        RoomEntity room = new RoomEntity();
        room.setNumber(101);

        ShelfEntity shelf1 = new ShelfEntity();
        shelf1.setId(1);
        shelf1.setLetter("A");
        shelf1.setColour("#FF0000");
        shelf1.setRoom(room);
        shelf1.setLevels(Collections.emptyList());

        ShelfEntity shelf2 = new ShelfEntity();
        shelf2.setId(2);
        shelf2.setLetter("B");
        shelf2.setColour("#00FF00");
        shelf2.setRoom(room);
        shelf2.setLevels(Collections.emptyList());

        List<ShelfEntity> shelfList = Arrays.asList(shelf1, shelf2);

        when(shelfRepository.findAll()).thenReturn(shelfList);

        List<ShelfDto> result = shelfService.getAllShelves();
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);

        assertThat(result.get(0).getLetter()).isEqualTo("A");
        assertThat(result.get(0).getColour()).isEqualTo("#FF0000");

        assertThat(result.get(1).getLetter()).isEqualTo("B");
        assertThat(result.get(1).getColour()).isEqualTo("#00FF00");

        verify(shelfRepository, times(1)).findAll();
    }

    @Test
    void getAllShelves_WhenEmpty_ShouldReturnEmptyList() {
        when(shelfRepository.findAll()).thenReturn(Collections.emptyList());

        List<ShelfDto> result = shelfService.getAllShelves();
        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isTrue();

        verify(shelfRepository, times(1)).findAll();
    }

    @Test
    void getShelvesByRoom_WhenRoomExists_ShouldReturnShelves() {
        int roomNumber = 101;

        RoomEntity room = new RoomEntity();
        room.setNumber(101);

        ShelfEntity shelf1 = new ShelfEntity();
        shelf1.setId(1);
        shelf1.setLetter("A");
        shelf1.setColour("#FF0000");
        shelf1.setRoom(room);
        shelf1.setLevels(Collections.emptyList());

        ShelfEntity shelf2 = new ShelfEntity();
        shelf2.setId(2);
        shelf2.setLetter("B");
        shelf2.setColour("#00FF00");
        shelf2.setRoom(room);
        shelf2.setLevels(Collections.emptyList());

        room.setShelves(Arrays.asList(shelf1, shelf2));

        when(roomRepository.findByNumber(roomNumber)).thenReturn(Optional.of(room));
        List<ShelfDto> result = shelfService.getShelvesByRoom(roomNumber);
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);

        assertThat(result.get(0).getLetter()).isEqualTo("A");
        assertThat(result.get(1).getLetter()).isEqualTo("B");

        verify(roomRepository, times(1)).findByNumber(roomNumber);
    }

    @Test
    void getShelvesByRoom_WhenRoomNotFound_ShouldThrowError() {
        int roomNumber = 999;
        when(roomRepository. findByNumber(roomNumber)).thenReturn(Optional. empty());

        assertThatThrownBy(() -> shelfService.getShelvesByRoom(roomNumber)).isInstanceOf(IllegalStateException.class).hasMessageContaining("Room not found");
    }

    @Test
    void deleteShelf_WhenNoPackages_ShouldDeleteSuccessfully() {
        int roomNumber = 101;
        Long shelfId = 5L;

        RoomEntity room = new RoomEntity();
        room.setNumber(101);

        ShelfEntity shelfToDelete = new ShelfEntity();
        shelfToDelete.setId(5);
        shelfToDelete. setLetter("C");
        shelfToDelete. setRoom(room);

        when(shelfRepository.findByIdAndRoom_Number(5, 101)).thenReturn(Optional.of(shelfToDelete));
        when(shelfRepository.isShelfOccupied(101, "C")).thenReturn(0L);
        boolean result = shelfService.deleteShelf(roomNumber, shelfId);
        assertThat(result).isTrue();
        verify(shelfRepository, times(1)).delete(shelfToDelete);
    }

    @Test
    void deleteShelf_WhenHasPackages_ShouldThrowError() {
        int roomNumber = 101;
        Long shelfId = 5L;

        RoomEntity room = new RoomEntity();
        room.setNumber(101);

        ShelfEntity shelf = new ShelfEntity();
        shelf.setId(5);
        shelf.setLetter("C");
        shelf.setRoom(room);

        when(shelfRepository.findByIdAndRoom_Number(5, 101)).thenReturn(Optional.of(shelf));
        when(shelfRepository.isShelfOccupied(101, "C")).thenReturn(3L);

        assertThatThrownBy(() -> shelfService.deleteShelf(roomNumber, shelfId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot delete shelf")
                .hasMessageContaining("3 packages left");

        verify(shelfRepository, never()).delete(any());
    }

    @Test
    void deleteShelf_WhenShelfNotFound_ShouldReturnFalse() {
        int roomNumber = 999;
        Long shelfId = 999L;

        when(shelfRepository.findByIdAndRoom_Number(999, 999)).thenReturn(Optional.empty());
        boolean result = shelfService. deleteShelf(roomNumber, shelfId);

        assertThat(result).isFalse();
        verify(shelfRepository, never()).delete(any());
    }

    @Test
    void getAllRoomNumbers_ShouldReturnAllNumbers() {
        List<Integer> roomNumbers = Arrays.asList(101, 102, 103, 201, 202);
        when(roomRepository.findALlRoomNumbers()).thenReturn(roomNumbers);
        List<Integer> result = shelfService.getAllRoomNumbers();
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(5);
        assertThat(result.get(0)).isEqualTo(101);
        assertThat(result.get(1)).isEqualTo(102);
        assertThat(result.get(4)).isEqualTo(202);

        verify(roomRepository, times(1)).findALlRoomNumbers();
    }
    @Test
    void updateSectionFullStatus_WhenSuccess_ShouldReturnTrue() {
        String shelfName = "A";
        Integer levelNumber = 2;
        Integer sectionId = 3;
        boolean isFull = true;

        when(sectionRepository.updateIsFull(shelfName, levelNumber, sectionId, isFull)).thenReturn(1);
        boolean result = shelfService.updateSectionFullStatus(shelfName, levelNumber, sectionId, isFull);
        assertThat(result).isTrue();
        verify(sectionRepository, times(1)).updateIsFull(shelfName, levelNumber, sectionId, isFull);
    }

    @Test
    void updateSectionFullStatus_WhenFails_ShouldReturnFalse() {
        String shelfName = "Z";
        Integer levelNumber = 99;
        Integer sectionId = 99;
        boolean isFull = true;
        when(sectionRepository.updateIsFull(shelfName, levelNumber, sectionId, isFull)).thenReturn(0);

        boolean result = shelfService.updateSectionFullStatus(shelfName, levelNumber, sectionId, isFull);
        assertThat(result).isFalse();
    }
}
