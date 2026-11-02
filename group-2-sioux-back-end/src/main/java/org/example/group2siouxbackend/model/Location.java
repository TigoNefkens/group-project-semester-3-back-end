package org.example.group2siouxbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @JsonProperty("location_id")
    private Long location_id;

    private int room;
    private String shelf;
    private int level;
    private int section;
}