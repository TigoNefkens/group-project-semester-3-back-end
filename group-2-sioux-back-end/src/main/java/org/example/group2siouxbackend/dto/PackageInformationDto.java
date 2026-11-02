package org.example.group2siouxbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PackageInformationDto {
    private String recipientName;
    private Long packageInformationId;
    private String status;
    private LocalDate arrivalDate;
    private LocalDate pickupDate;
    private String email;
}
