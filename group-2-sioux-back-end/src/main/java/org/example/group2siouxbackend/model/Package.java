package org.example.group2siouxbackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Package {
    @JsonProperty("package_id")
    private Long packageId;

    @JsonProperty("location_id")
    private Long locationId;

    @JsonProperty("package_information_id")
    private Long packageInformationId;
}