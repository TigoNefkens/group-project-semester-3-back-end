package org.example.group2siouxbackend.controller;

import org.example.group2siouxbackend.dto.RoomDto;
import org.example.group2siouxbackend.dto.RoomNameDto;
import org.example.group2siouxbackend.dto.ShelfDto;
import org.example.group2siouxbackend.business.ShelfService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/Shelves")
public class ShelfController {

    private final ShelfService shelfService;

    public ShelfController(ShelfService shelfService) {
        this.shelfService = shelfService;
    }

    @GetMapping
    public List<ShelfDto> getAllShelves() {
        return shelfService.getAllShelves();
    }

    @GetMapping("/rooms/{roomNumber}/shelves")
    public ResponseEntity<List<ShelfDto>> getShelvesByRoom(
            @PathVariable int roomNumber) {

        try {
            List<ShelfDto> shelves = shelfService.getShelvesByRoom(roomNumber);
            return ResponseEntity.ok(shelves);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/rooms")
    public List<RoomNameDto> getAllRoomNames() {
        return shelfService.getAllRoomNames();
    }

    @PatchMapping("/rooms/{roomNumber}/shelves/{shelfName}/levels/{levelId}/sections/{sectionId}")
    public ResponseEntity<String> updateSectionFullStatus(
            @PathVariable int roomNumber,
            @PathVariable String shelfName,
            @PathVariable int levelId,
            @PathVariable int sectionId,
            @RequestParam boolean isFull) {

        boolean updated = shelfService.updateSectionFullStatus(shelfName, levelId, sectionId, isFull);

        if (updated) {
            return ResponseEntity.ok("Section updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Section not found");
        }
    }

    @PutMapping("/{roomNumber}/{shelfId}")
    public ResponseEntity<String> editShelf(
            @PathVariable int roomNumber,
            @PathVariable Long shelfId,
            @RequestBody ShelfDto shelfDto) {

        boolean updated = shelfService.editShelf(roomNumber, shelfId, shelfDto);

        if (updated) {
            return ResponseEntity.ok("Shelf updated successfully: " + shelfDto.getLetter());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Shelf with ID " + shelfId + " not found room " + roomNumber + ".");
        }
    }

    @PostMapping("/shelves/{roomNumber}")
    public ResponseEntity<String> createShelf(
            @PathVariable int roomNumber,
            @RequestBody ShelfDto shelfDto) {

        shelfService.createShelf(roomNumber, shelfDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Shelf created successfully in room number " + roomNumber);
    }

    @PostMapping("/rooms")
    public ResponseEntity<String> createRoom(@RequestBody RoomDto roomDto) {
        try {
            boolean created = shelfService.createRoom(roomDto);
            if (created) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body("Room " + roomDto.getNumber() + " (" + roomDto.getName() + ") created successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Room " + roomDto.getNumber() + " already exists.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating room: " + e.getMessage());
        }
    }

    @DeleteMapping("/{roomNumber}/{shelfId}")
    public ResponseEntity<String> deleteShelf(
            @PathVariable int roomNumber,
            @PathVariable Long shelfId) {

        boolean deleted = shelfService.deleteShelf(roomNumber, shelfId);

        if (deleted) {
            return ResponseEntity.ok("Shelf deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Shelf with ID " + shelfId + " not found in room " + roomNumber + ".");
        }
    }
}
