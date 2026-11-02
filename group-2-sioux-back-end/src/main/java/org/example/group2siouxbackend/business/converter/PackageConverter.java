package org.example.group2siouxbackend.business.converter;

import lombok.RequiredArgsConstructor;
import org.example.group2siouxbackend.model.Package;
import org.example.group2siouxbackend.persistence.entity.LocationEntity;
import org.example.group2siouxbackend.persistence.entity.PackageEntity;
import org.example.group2siouxbackend.persistence.entity.PackageInformationEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PackageConverter {
    public static PackageEntity fromDomainToEntity(Package domain){
        return PackageEntity.builder()
                .id(null)
                .packageInformationEntity(PackageInformationEntity.builder().id(Math.toIntExact(domain.getPackageInformationId())).build())
                .locationEntity(LocationEntity.builder().id(Math.toIntExact(domain.getLocationId())).build())
                .build();
    }

    public static Package fromEntityToDomain(PackageEntity entity){
        return Package.builder()
                .packageId(Long.valueOf(entity.getId()))
                .packageInformationId(Long.valueOf(entity.getPackageInformationEntity().getId()))
                .locationId(Long.valueOf(entity.getLocationEntity().getId()))
                .build();
    }
}
