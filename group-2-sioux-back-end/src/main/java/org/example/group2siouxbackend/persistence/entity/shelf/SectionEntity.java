package org.example.group2siouxbackend.persistence.entity.shelf;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "section")
public class SectionEntity {
    @EmbeddedId
    private SectionId id;

    @Column(name = "is_full", nullable = false)
    private Boolean isFull;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(
                    name = "shelf_id",
                    referencedColumnName = "shelf_id",
                    insertable = false,
                    updatable = false
            ),
            @JoinColumn(
                    name = "level_id",
                    referencedColumnName = "number",
                    insertable = false,
                    updatable = false
            )
    })
    private LevelEntity level;


    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class SectionId implements Serializable {

        @Column(name = "shelf_id")
        private Integer shelfId;

        @Column(name = "level_id")
        private Integer levelNumber;

        @Column(name = "id")
        private Integer sectionId;


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SectionId that)) return false;
            return Objects.equals(shelfId, that.shelfId)
                    && Objects.equals(levelNumber, that.levelNumber)
                    && Objects.equals(sectionId, that.sectionId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(shelfId, levelNumber, sectionId);
        }
    }

}