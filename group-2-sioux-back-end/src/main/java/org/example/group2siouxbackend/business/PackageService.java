package org.example.group2siouxbackend.business;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.group2siouxbackend.business.converter.PackageConverter;
import org.example.group2siouxbackend.dto.LocationCountDto;
import org.example.group2siouxbackend.dto.LocationDto;
import org.example.group2siouxbackend.dto.PackageInformationDto;
import org.example.group2siouxbackend.dto.PackageInformationResponse;
import org.example.group2siouxbackend.dto.PackageSaveRequest;
import org.example.group2siouxbackend.dto.PackagesDto;
import org.example.group2siouxbackend.model.Location;
import org.example.group2siouxbackend.model.Package;
import org.example.group2siouxbackend.model.enums.PackageStatus;
import org.example.group2siouxbackend.persistence.entity.LocationEntity;
import org.example.group2siouxbackend.persistence.entity.PackageEntity;
import org.example.group2siouxbackend.persistence.entity.PackageInformationEntity;
import org.example.group2siouxbackend.persistence.entity.shelf.LevelEntity;
import org.example.group2siouxbackend.persistence.entity.shelf.SectionEntity;
import org.example.group2siouxbackend.persistence.entity.shelf.ShelfEntity;
import org.example.group2siouxbackend.persistence.interfaces.LocationRepository;
import org.example.group2siouxbackend.persistence.interfaces.PackageInformationRepository;
import org.example.group2siouxbackend.persistence.interfaces.PackageRepository;
import org.example.group2siouxbackend.persistence.interfaces.shelves.ShelfRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PackageService {
    private final ShelfRepository shelfRepository;
    private final LocationService locationService;
    private final LocationRepository locationRepository;
    private final PackageInformationRepository packageInformationRepository;
    private final PackageRepository packageRepository;
    private final ShelfService shelfService;
    public List<LocationCountDto> getPackageCountPerLocation(Long roomId) {
        List<PackageEntity> entities = packageRepository.findPackagesByLocationEntity_Room(Math.toIntExact(roomId));

        List<Package> packagesInRoom = entities.stream()
                .map(PackageConverter::fromEntityToDomain)
                .toList();

        // Map with composite key (section + shelf + level)
        Map<String, Long> locationCount = new HashMap<>();

        for (Package pkg : packagesInRoom) {
            locationRepository.findLocationEntityById(Math.toIntExact(pkg.getLocationId()))
                    .ifPresent(loc -> {
                        String key = loc.getShelf() + "|" + loc.getLevel() + "|" + loc.getSection();
                        locationCount.put(key, locationCount.getOrDefault(key, 0L) + 1);
                    });
        }

        // Convert to DTOs
        List<LocationCountDto> result = new ArrayList<>();
        locationCount.forEach((key, count) -> {
            String[] parts = key.split("\\|");
            result.add(new LocationCountDto(parts[0], Integer.parseInt(parts[1]), parts[2], count));
        });

        return result;
    }
    @Transactional
    public void createPackage(PackageSaveRequest request) {
        //if i relocate package i need to remove mark as full from the section
        if (!shelfService.findShelfByLocation(request.getRoom(), request.getShelf(), request.getLevel(), request.getSection()))
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This location is not found");
        }

        if (packageInformationRepository.findById(request.getPackageInformationId()).isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Package Information not found");
        }

        Location loc = new Location(null, request.getRoom(), request.getShelf(), request.getLevel(), request.getSection());

        Long locationId = locationService.checkForLocation(loc);
        if (locationId == null)
        {
            locationId = locationService.createLocation(loc);
        }
        Package newPackage = new Package(null, locationId, (long)request.getPackageInformationId());

        if(packageInformationRepository.updateStatusAndArrivalDate(request.getPackageInformationId(), PackageStatus.STORED.name()) == 0)
        {
            throw new ResponseStatusException(HttpStatus.NOT_MODIFIED, "Package Information is not updated");
        }

        packageRepository.save(PackageConverter.fromDomainToEntity(newPackage));
    }

    public LocationDto findLocationByVerification(String verificationCode) {

        PackageInformationEntity info = packageInformationRepository
                .findByVerification(verificationCode)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Verification code not found"));

        PackageEntity pkg = packageRepository
                .findByPackageInformationEntity(info)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Package not stored"));

        LocationEntity loc = locationRepository.findLocationEntityById(pkg.getLocationEntity().getId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Location not found"));

        return new LocationDto(
                loc.getId().longValue(),
                loc.getRoom(),
                loc.getShelf(),
                loc.getLevel(),
                loc.getSection()
        );
    }

    @Transactional
    public List<PackagesDto> getAllPackages() {

        return packageRepository.findAll().stream()
                .map(pkg -> new PackagesDto(
                        (long)pkg.getId(),
                        (long)pkg.getLocationEntity().getId(),
                        (long)pkg.getPackageInformationEntity().getId()
                ))
                .collect(Collectors.toList());
    }
    @Transactional
    public List<LocationDto> getAllLocations() {
        List<ShelfEntity> shelves = shelfRepository.findAll();

        List<LocationDto> result = new ArrayList<>();

        for (ShelfEntity shelf : shelves) {
            int room = shelf.getRoom().getNumber();
            String shelfLetter = shelf.getLetter();

            for (LevelEntity level : shelf.getLevels()) {
                int levelNumber = level.getId().getNumber();

                for (SectionEntity section : level.getSections()) {
                    result.add(new LocationDto(
                            null,
                            room,
                            shelfLetter,
                            levelNumber,
                            section.getId().getSectionId()
                    ));
                }
            }
        }

        return result;
    }

    @Transactional
    public List<PackageInformationDto> getAllPackageInformation() {

        return packageInformationRepository.findAll().stream()
                .map(info -> new PackageInformationDto(
                        info.getRecipientName(),
                        (long)info.getId(),
                        info.getStatus(),
                        info.getArrivalDate() == null ? null :
                                info.getArrivalDate().atZone(ZoneId.systemDefault()).toLocalDate(),

                        info.getPickupDate() == null ? null :
                                info.getPickupDate().atZone(ZoneId.systemDefault()).toLocalDate(),

                        info.getEmail()
                ))
                .collect(Collectors.toList());
    }
@Transactional
    public PackageInformationResponse getPackageInformationByPackageId(Long packageId) {
    PackageEntity entity = packageRepository.findById(Math.toIntExact(packageId))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Package Information not found"));

    return new PackageInformationResponse(
            entity.getPackageInformationEntity().getRecipientName(),
            (long) entity.getPackageInformationEntity().getId()
    );}

    public LocationDto getLocationById(long locationId) {
        LocationEntity loc = locationRepository.findLocationEntityById((int) locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));

        return new LocationDto(
                loc.getId().longValue(),
                loc.getRoom(),
                loc.getShelf(),
                loc.getLevel(),
                loc.getSection()
        );
    }


    public PackageInformationResponse getPackageInformationByVerificationCode(String verification) {
        PackageInformationEntity entity = packageInformationRepository
                .findByVerification(verification)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Package Information not found"
                ));

        if(entity == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Package Information not found");

        return new PackageInformationResponse(
                entity.getRecipientName(),
                (long) entity.getId()
        );
    }

    @Transactional
    public void markPackagePickedUp(String verificationCode) {
        PackageInformationEntity info = packageInformationRepository
                .findByVerification(verificationCode)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Package Information not found"));

        info.setPickupDate(java.time.Instant.now());

        info.setStatus("Delivered");
        info.setStored(false);

        packageInformationRepository.save(info);
    }

    @Transactional
    public void deletePackageLocation(String verificationCode) {
        PackageInformationEntity info = packageInformationRepository
                .findByVerification(verificationCode)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Package Information not found"));

        packageRepository.findByPackageInformationEntity(info)
                .ifPresent(packageRepository::delete);
    }
}
