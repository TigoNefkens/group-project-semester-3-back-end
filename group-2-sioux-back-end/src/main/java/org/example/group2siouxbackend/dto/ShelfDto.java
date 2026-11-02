package org.example.group2siouxbackend.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShelfDto {
    private Long id;
    private String letter;
    private String colour;
    private List<LevelDto> levels;
}