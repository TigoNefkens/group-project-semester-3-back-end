package org.example.group2siouxbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor

public class LocationDto {
    private Long location_id;

    private int room;
    private String shelf;
    private int level;
    private int section;
}
