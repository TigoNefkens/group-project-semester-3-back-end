package org.example.group2siouxbackend.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "package_information", schema = "sioux_platform")
public class PackageInformationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "recipient_name", nullable = false)
    private String recipientName;

    @Column(name = "email", nullable = false)
    private String email;

    @Lob
    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "pickup_date")
    private Instant pickupDate;

    @Column(name = "arrival_date")
    private Instant arrivalDate;

    @Column(name = "verification", nullable = false, length = 32)
    private String verification;

    @ColumnDefault("0")
    @Column(name = "`stored`", nullable = false)
    private Boolean stored = false;

}