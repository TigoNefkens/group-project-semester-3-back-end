package org.example.group2siouxbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LocationCountDto {
    private String shelf;
    private Integer level;
    private String section;
    private Long packageCount;
}
