package org.example.group2siouxbackend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "package", schema = "sioux_platform")
public class PackageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private LocationEntity locationEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_information_id")
    private PackageInformationEntity packageInformationEntity;

}