package org.example.group2siouxbackend.persistence.entity.shelf;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shelf")
public class ShelfEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "letter", nullable = false, length = 32)
    private String letter;

    @ColumnDefault("'#000000'")
    @Column(name = "colour", nullable = false, length = 16)
    private String colour;

    @OneToMany(mappedBy = "shelf", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LevelEntity> levels = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private RoomEntity room;

}