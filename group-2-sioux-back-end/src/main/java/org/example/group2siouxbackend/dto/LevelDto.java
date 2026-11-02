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
public class LevelDto {
    private Long id;
    private List<SectionDto> sections;
}