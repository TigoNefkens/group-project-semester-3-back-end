package org.example.group2siouxbackend.persistence.entity.shelf;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@Table(name = "level")
@NoArgsConstructor
@AllArgsConstructor
public class LevelEntity {

    @EmbeddedId
    private LevelId id;

    @MapsId("shelfId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shelf_id", nullable = false)
    private ShelfEntity shelf;

    @OneToMany(mappedBy = "level", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SectionEntity> sections = new ArrayList<>();

    public LevelEntity(LevelId levelId) {
        this.id = levelId;
    }

    @Getter
    @Setter
    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LevelId implements Serializable {

        @Column(name = "shelf_id")
        private Integer shelfId;

        @Column(name = "number")
        private Integer number;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LevelId levelId = (LevelId) o;
            return Objects.equals(shelfId, levelId.shelfId)
                    && Objects.equals(number, levelId.number);
        }

        @Override
        public int hashCode() {
            return Objects.hash(shelfId, number);
        }
    }

}