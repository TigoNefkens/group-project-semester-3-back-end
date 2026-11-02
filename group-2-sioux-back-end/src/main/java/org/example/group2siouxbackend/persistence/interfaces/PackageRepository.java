package org.example.group2siouxbackend.persistence.interfaces;

import org.example.group2siouxbackend.persistence.entity.PackageEntity;
import org.example.group2siouxbackend.persistence.entity.PackageInformationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PackageRepository extends JpaRepository<PackageEntity, Integer> {
    List<PackageEntity> findPackagesByLocationEntity_Room(Integer room_id);

    Optional<PackageEntity> findByPackageInformationEntity(PackageInformationEntity info);
}