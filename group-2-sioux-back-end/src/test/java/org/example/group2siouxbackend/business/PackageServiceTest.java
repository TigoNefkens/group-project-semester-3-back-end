package org.example.group2siouxbackend.business;

import org.example.group2siouxbackend.dto.*;
import org.example.group2siouxbackend.model.enums.PackageStatus;
import org.example.group2siouxbackend.persistence.entity.LocationEntity;
import org.example.group2siouxbackend.persistence.entity.PackageEntity;
import org.example.group2siouxbackend.persistence.entity.PackageInformationEntity;
import org.example.group2siouxbackend.persistence.entity.shelf.LevelEntity;
import org.example.group2siouxbackend.persistence.entity.shelf.RoomEntity;
import org.example.group2siouxbackend.persistence.entity.shelf.SectionEntity;
import org.example.group2siouxbackend.persistence.entity.shelf.ShelfEntity;
import org.example.group2siouxbackend.persistence.interfaces.LocationRepository;
import org.example.group2siouxbackend.persistence.interfaces.PackageInformationRepository;
import org.example.group2siouxbackend.persistence.interfaces.PackageRepository;
import org.example.group2siouxbackend.persistence.interfaces.shelves.ShelfRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PackageServiceTest {
    @Mock
    private LocationService locationService;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private PackageInformationRepository packageInformationRepository;

    @Mock
    private PackageRepository packageRepository;

    @Mock
    private ShelfService shelfService;

    @Mock
    private ShelfRepository shelfRepository;

    @InjectMocks
    private PackageService packageService;

    @Test
    public void createPackage_WhenShelfNotFound_ShouldThrowError() {
        PackageSaveRequest request = new PackageSaveRequest(1, 101, "A", 2, 3);

        when(shelfService.findShelfByLocation(101, "A", 2, 3)).thenReturn(false);

        assertThatThrownBy(() -> packageService.createPackage(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("This location is not found");
        verify(packageRepository, never()).save(any());
    }

    @Test
    void createPackage_WhenPackageInNotFound_ShouldThrowError() {
        PackageSaveRequest request = new PackageSaveRequest(999, 101, "A", 2, 3);
        when(shelfService.findShelfByLocation(anyInt(), anyString(), anyInt(), anyInt())).thenReturn(true);
        when(packageInformationRepository.findById(999)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> packageService.createPackage(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Package Information not found");
        verify(packageRepository, never()).save(any());
    }

    @Test
    void createPackage_WhenEverithingIsOk_ShouldSavePackage() {
        PackageSaveRequest request = new PackageSaveRequest(1, 101, "A", 2, 3);
        PackageInformationEntity packageInfo = PackageInformationEntity.builder()
                .id(1)
                .recipientName("Juan Pérez")
                .email("juan@example.com")
                .status("ARRIVED")
                .verification("ABC123")
                .build();

        when(shelfService.findShelfByLocation(101, "A", 2, 3)).thenReturn(true);
        when(packageInformationRepository.findById(1)).thenReturn(Optional.of(packageInfo));
        when(locationService.checkForLocation(any())).thenReturn(5L);
        when(packageInformationRepository.updateStatusAndArrivalDate(1, PackageStatus.STORED.name())).thenReturn(1);

        packageService.createPackage(request);

        verify(packageRepository, times(1)).save(any());
        verify(locationService, never()).createLocation(any());
    }

    @Test
    void createPackage_WhenLocationDoesNotExist_ShouldCreateIt() {
        PackageSaveRequest request = new PackageSaveRequest(1, 101, "A", 2, 3);
        PackageInformationEntity packageInfo = PackageInformationEntity.builder()
                .id(1)
                .recipientName("María López")
                .email("maria@example.com")
                .status("ARRIVED")
                .verification("XYZ789")
                .build();

        when(shelfService.findShelfByLocation(101, "A", 2, 3)).thenReturn(true);
        when(packageInformationRepository.findById(1)).thenReturn(Optional.of(packageInfo));

        when(locationService.checkForLocation(any())).thenReturn(null);

        when(locationService.createLocation(any())).thenReturn(10L);
        when(packageInformationRepository.updateStatusAndArrivalDate(anyInt(), anyString())).thenReturn(1);

        packageService.createPackage(request);

        verify(locationService, times(1)).createLocation(any());
        verify(packageRepository, times(1)).save(any());
    }
    @Test
    void findLocationByVerification_WhenPackageExists_ShouldReturnLocation() {
        String verification = "ABC123";
        PackageInformationEntity packageInfo = PackageInformationEntity.builder()
                .id(1)
                .verification(verification)
                .recipientName("Juan Pérez")
                .build();

        LocationEntity locationEntity = LocationEntity.builder()
                .id(5)
                .room(101)
                .shelf("A")
                .level(2)
                .section(3)
                .build();
        PackageEntity packageEntity = PackageEntity.builder()
                .id(10)
                .packageInformationEntity(packageInfo)
                .locationEntity(locationEntity)
                .build();

        when(packageInformationRepository.findByVerification(verification)).thenReturn(Optional.of(packageInfo));
        when(packageRepository. findById(1)).thenReturn(Optional.of(packageEntity));
        when(locationRepository.findLocationEntityById(5)).thenReturn(Optional.of(locationEntity));

        LocationDto result = packageService.findLocationByVerification(verification);

        assertThat(result).isNotNull();
        assertThat(result.getLocation_id()).isEqualTo(5L);
        assertThat(result.getRoom()).isEqualTo(101);
        assertThat(result.getShelf()).isEqualTo("A");
        assertThat(result.getLevel()).isEqualTo(2);
        assertThat(result.getSection()).isEqualTo(3);

        verify(packageInformationRepository, times(1)).findByVerification(verification);
        verify(packageRepository, times(1)).findById(1);
        verify(locationRepository, times(1)).findLocationEntityById(5);
    }

    @Test
    void findLocationByVerification_WhenCodeNotFound_ShouldThrowError() {
        String verification = "NOEXIST";

        when(packageInformationRepository.findByVerification(verification)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> packageService.findLocationByVerification(verification))
            .isInstanceOf(ResponseStatusException.class)
            .hasMessageContaining("Verification code not found");
        verify(packageRepository, never()).findById(anyInt());
        verify(locationRepository, never()).findLocationEntityById(anyInt());
    }

    @Test
    void findLocationByVerification_WhenPackageNotStored_ShouldThrowError() {
        String verification = "ABC123";

        PackageInformationEntity packageInfo = PackageInformationEntity.builder()
                .id(1)
                .verification(verification)
                .build();

        when(packageInformationRepository.findByVerification(verification)).thenReturn(Optional.of(packageInfo));
        when(packageRepository. findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> packageService.findLocationByVerification(verification))
            .isInstanceOf(ResponseStatusException.class)
            .hasMessageContaining("Package not stored");

        verify(locationRepository, never()).findLocationEntityById(anyInt());
    }

    @Test
    void getAllLocations_ShouldReturnAllLocations() {
            RoomEntity room = new RoomEntity();
            room.setNumber(101);

            ShelfEntity shelfA = ShelfEntity.builder()
                    .id(1)
                    .letter("A")
                    .colour("#FFFFFF")
                    .room(room)
                    .levels(new ArrayList<>())
                    .build();

            LevelEntity.LevelId levelId = new LevelEntity.LevelId(1, 1);
            LevelEntity level1 = LevelEntity.builder()
                    .id(levelId)
                    .shelf(shelfA)
                    .sections(new ArrayList<>())
                    .build();
            shelfA.getLevels().add(level1);

            SectionEntity.SectionId sectionId = new SectionEntity.SectionId(1, 1, 10);
            SectionEntity section = SectionEntity.builder()
                    .id(sectionId)
                    .level(level1)
                    .isFull(false)
                    .build();
            level1.getSections().add(section);

            when(shelfRepository.findAll()).thenReturn(List.of(shelfA));

            List<LocationDto> result = packageService.getAllLocations();

            assertThat(result).isNotNull();
            assertThat(result.get(0).getRoom()).isEqualTo(101);
            assertThat(result.get(0).getShelf()).isEqualTo("A");
            assertThat(result.get(0).getLevel()).isEqualTo(1);
            assertThat(result.get(0).getSection()).isEqualTo(10);

            verify(shelfRepository, times(1)).findAll();
        }

    @Test
    void getAllLocations_WhenEmpty_ShouldReturnEmptyList() {
        when(shelfRepository.findAll()).thenReturn(Collections.emptyList());
        List<LocationDto> result = packageService.getAllLocations();

        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isTrue();

        verify(shelfRepository, times(1)).findAll();

    }

    @Test
    void getAllPackageInformation_ShouldReturnAllInfo() {
        List<PackageInformationEntity> packageInfoList = Arrays.asList(
                PackageInformationEntity.builder()
                        .id(1)
                        .recipientName("Juan Pérez")
                        .status("STORED")
                        .email("juan@example.com")
                        .arrivalDate(null)
                        .pickupDate(null)
                        .build(),
                PackageInformationEntity.builder()
                        . id(2)
                        . recipientName("María López")
                        .status("DELIVERED")
                        .email("maria@example.com")
                        .arrivalDate(null)
                        .pickupDate(null)
                        .build()
        );
        when(packageInformationRepository.findAll()).thenReturn(packageInfoList);

        List<PackageInformationDto> result = packageService.getAllPackageInformation();

        assertThat(result.get(0).getRecipientName()).isEqualTo("Juan Pérez");
        assertThat(result.get(0).getStatus()).isEqualTo("STORED");
        assertThat(result.get(0).getEmail()).isEqualTo("juan@example.com");
        assertThat(result.get(1).getRecipientName()).isEqualTo("María López");
        assertThat(result.get(1).getStatus()).isEqualTo("DELIVERED");

        verify(packageInformationRepository, times(1)).findAll();
    }

    @Test
    void getPackageInformationByPackageId_WhenExists_ShouldReturnInfo() {
        Long packageId = 10L;

        PackageInformationEntity packageInfo = PackageInformationEntity. builder()
                .id(1)
                .recipientName("Carlos García")
                .build();

        PackageEntity packageEntity = PackageEntity.builder()
                .id(10)
                .packageInformationEntity(packageInfo)
                .build();

        when(packageRepository.findById(10)).thenReturn(Optional.of(packageEntity));

        PackageInformationResponse result = packageService.getPackageInformationByPackageId(packageId);

        assertThat(result).isNotNull();
        assertThat(result.getRecipientName()).isEqualTo("Carlos García");
        assertThat(result.getPackageInformationId()).isEqualTo(1L);

        verify(packageRepository, times(1)).findById(10);
    }

    @Test
    void getLocationById_WhenExists_ShouldReturnLocation() {
        long locationId = 5L;

        LocationEntity locationEntity = LocationEntity.builder()
                .id(5)
                .room(101)
                .shelf("A")
                .level(2)
                .section(3)
                .build();

        when(locationRepository.findLocationEntityById(5)).thenReturn(Optional.of(locationEntity));

        LocationDto result = packageService.getLocationById(locationId);

        assertThat(result).isNotNull();
        assertThat(result.getLocation_id()).isEqualTo(5L);
        assertThat(result.getRoom()).isEqualTo(101);
        assertThat(result.getShelf()).isEqualTo("A");
        assertThat(result.getLevel()).isEqualTo(2);
        assertThat(result.getSection()).isEqualTo(3);

        verify(locationRepository, times(1)).findLocationEntityById(5);
    }

    @Test
    void getPackageInformationByVerificationCode_WhenExists_ShouldReturnInfo() {
        String verificationCode = "ABC123";

        PackageInformationEntity packageInfo = PackageInformationEntity.builder()
                .id(1)
                .verification(verificationCode)
                .recipientName("Ana Martínez")
                .build();

        when(packageInformationRepository.findByVerification(verificationCode)).thenReturn(Optional.of(packageInfo));

        PackageInformationResponse result = packageService.getPackageInformationByVerificationCode(verificationCode);

        assertThat(result).isNotNull();
        assertThat(result.getRecipientName()).isEqualTo("Ana Martínez");
        assertThat(result.getPackageInformationId()).isEqualTo(1L);

        verify(packageInformationRepository, times(1)).findByVerification(verificationCode);
    }

    @Test
    void getPackageInformationByVerificationCode_WhenNotFound_ShouldThrowError() {
        String verificationCode = "NOEXIST";

        when(packageInformationRepository.findByVerification(verificationCode)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> packageService.getPackageInformationByVerificationCode(verificationCode))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Package Information not found");
    }

    @Test
    void getPackageCountPerLocation_ShouldGroupAndCountPackages() {
        Long roomId = 101L;

        LocationEntity location1 = LocationEntity.builder()
                .id(1)
                .room(101)
                .shelf("A")
                .level(1)
                .section(2)
                .build();

        LocationEntity location2 = LocationEntity.builder()
                .id(2)
                .room(101)
                .shelf("B")
                .level(2)
                .section(3)
                .build();
        LocationEntity loc1 = LocationEntity.builder().id(1).build();
        LocationEntity loc2 = LocationEntity.builder().id(2).build();

        PackageInformationEntity info1 = PackageInformationEntity.builder().id(1).build();
        PackageInformationEntity info2 = PackageInformationEntity. builder().id(2).build();
        PackageInformationEntity info3 = PackageInformationEntity. builder().id(3).build();
        PackageInformationEntity info4 = PackageInformationEntity.builder().id(4).build();
        PackageInformationEntity info5 = PackageInformationEntity.builder().id(5).build();

        List<PackageEntity> packages = Arrays.asList(
                PackageEntity.builder()
                        .id(10)
                        .locationEntity(loc1)
                        . packageInformationEntity(info1)
                        .build(),
                PackageEntity.builder()
                        .id(11)
                        .locationEntity(loc1)
                        .packageInformationEntity(info2)
                        .build(),
                PackageEntity.builder()
                        .id(12)
                        .locationEntity(loc1)
                        .packageInformationEntity(info3)
                        .build(),
                PackageEntity.builder()
                        .id(13)
                        .locationEntity(loc2)
                        .packageInformationEntity(info4)
                        .build(),
                PackageEntity.builder()
                        .id(14)
                        .locationEntity(loc2)
                        .packageInformationEntity(info5)
                        .build()
        );

        when(packageRepository.findPackagesByLocationEntity_Room(101)).thenReturn(packages);
        when(locationRepository.findLocationEntityById(1)).thenReturn(Optional.of(location1));
        when(locationRepository.findLocationEntityById(2)).thenReturn(Optional.of(location2));

        List<LocationCountDto> result = packageService.getPackageCountPerLocation(roomId);

        assertThat(result).isNotNull();
        assertThat(result. size()).isEqualTo(2);

        LocationCountDto locationA = result.stream()
                .filter(loc -> loc.getShelf().equals("A") &&
                        loc.getLevel() == 1 &&
                        loc.getSection().equals("2"))
                .findFirst()
                .orElse(null);

        assertThat(locationA).isNotNull();
        assertThat(locationA.getPackageCount()).isEqualTo(3L);

        LocationCountDto locationB = result.stream()
                .filter(loc -> loc.getShelf().equals("B") &&
                        loc. getLevel() == 2 &&
                        loc.getSection().equals("3"))
                .findFirst()
                .orElse(null);

        assertThat(locationB).isNotNull();
        assertThat(locationB.getPackageCount()).isEqualTo(2L);

        verify(packageRepository, times(1)).findPackagesByLocationEntity_Room(101);
        verify(locationRepository, times(5)).findLocationEntityById(anyInt());
    }

    @Test
    void getPackageCountPerLocation_WhenNoPackages_ShouldReturnEmptyList() {
        Long roomId = 999L;
        when(packageRepository. findPackagesByLocationEntity_Room(999)).thenReturn(Collections.emptyList());

        List<LocationCountDto> result = packageService.getPackageCountPerLocation(roomId);
        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isTrue();

        verify(packageRepository, times(1)).findPackagesByLocationEntity_Room(999);
        verify(locationRepository, never()).findLocationEntityById(anyInt());

    }

    @Test
    void getPackageCountPerLocation_WhenAllInSameLocation_ShouldGroupCorrectly() {
        Long roomId = 101L;

        LocationEntity sameLocation = LocationEntity.builder()
                .id(5)
                .room(101)
                .shelf("C")
                .level(3)
                .section(4)
                .build();

        LocationEntity loc = LocationEntity.builder().id(5).build();

        List<PackageEntity> packages = Arrays.asList(
                PackageEntity.builder()
                        .id(20)
                        .locationEntity(loc)
                        .packageInformationEntity(PackageInformationEntity.builder().id(1).build())
                        .build(),
                PackageEntity.builder()
                        . id(21)
                        . locationEntity(loc)
                        .packageInformationEntity(PackageInformationEntity.builder().id(2).build())
                        .build(),
                PackageEntity.builder()
                        .id(22)
                        .locationEntity(loc)
                        .packageInformationEntity(PackageInformationEntity.builder().id(3).build())
                        .build(),
                PackageEntity.builder()
                        .id(23)
                        .locationEntity(loc)
                        .packageInformationEntity(PackageInformationEntity.builder().id(4).build())
                        .build()
        );

        when(packageRepository.findPackagesByLocationEntity_Room(101)).thenReturn(packages);
        when(locationRepository.findLocationEntityById(5)).thenReturn(Optional.of(sameLocation));

        List<LocationCountDto> resultado = packageService.getPackageCountPerLocation(roomId);
        assertThat(resultado).isNotNull();
        assertThat(resultado.size()).isEqualTo(1);
        LocationCountDto location = resultado.get(0);
        assertThat(location.getShelf()).isEqualTo("C");
        assertThat(location.getLevel()).isEqualTo(3);
        assertThat(location. getSection()).isEqualTo("4");
        assertThat(location.getPackageCount()).isEqualTo(4L);
        verify(packageRepository, times(1)).findPackagesByLocationEntity_Room(101);
        verify(locationRepository, times(4)).findLocationEntityById(5);
    }
}
