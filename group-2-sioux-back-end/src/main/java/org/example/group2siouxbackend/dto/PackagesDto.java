package org.example.group2siouxbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PackagesDto {
    private Long packageId;
    private Long locationId;
    private Long packageInformationId;
}