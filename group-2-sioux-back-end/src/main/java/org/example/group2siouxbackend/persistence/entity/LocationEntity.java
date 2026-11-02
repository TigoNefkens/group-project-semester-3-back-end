package org.example.group2siouxbackend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "location", schema = "sioux_platform",uniqueConstraints = @UniqueConstraint(
        columnNames = {"room", "shelf", "level", "section"}
))
public class LocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "room", nullable = false)
    private Integer room;

    @Column(name = "shelf", nullable = false)
    private String shelf;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "section", nullable = false)
    private Integer section;
}