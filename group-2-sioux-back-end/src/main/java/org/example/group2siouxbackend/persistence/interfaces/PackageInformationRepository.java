package org.example.group2siouxbackend.persistence.interfaces;

import org.example.group2siouxbackend.model.enums.PackageStatus;
import org.example.group2siouxbackend.persistence.entity.PackageInformationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PackageInformationRepository extends JpaRepository<PackageInformationEntity, Integer> {
    Optional<PackageInformationEntity> findByVerification(String verification);

    @Modifying
    @Query("""

            update PackageInformationEntity p
    set p.status = :status,
        p.arrivalDate = current_timestamp
    where p.id = :id
    """)
    int updateStatusAndArrivalDate(
            Integer id,
            String status
    );
}
