package org.example.group2siouxbackend.persistence.entity.shelf;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "room")
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "number", nullable = false)
    private Integer number;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShelfEntity> shelves = new ArrayList<>();

}