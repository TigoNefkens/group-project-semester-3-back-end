package org.example.group2siouxbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PackageInformation {
    @JsonProperty("package_information_id")
    private Long packageInformationId;
    @JsonProperty("recipient_name")
    private String recipientName;
    private String status;
    @JsonProperty("arrival_date")
    private LocalDate arrivalDate;
    @JsonProperty("pickup_date")
    private LocalDate pickupDate;
    private String email;
}