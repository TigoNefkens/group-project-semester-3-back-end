package org.example.group2siouxbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class RoomDto {
    private int number;
    private String name;
    private List<ShelfDto> shelves  = new ArrayList<>();
}