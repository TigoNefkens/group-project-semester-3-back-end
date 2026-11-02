package org.example.group2siouxbackend.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Shelf {
    private Long id;
    private String letter;
    private String colour;
    private List<Level> levels = new ArrayList<>();
}