package org.example.group2siouxbackend.controller;

import org.example.group2siouxbackend.dto.*;
import org.example.group2siouxbackend.business.PackageService;
//import org.example.group2siouxbackend.business.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/packages")
@RequiredArgsConstructor
public class PackageController {

    private final PackageService service;
//    private final EmailService emailService;

    @PostMapping("/")
    public HttpStatus savePackage(@RequestBody PackageSaveRequest request) {
        try{
            service.createPackage(request);
            return HttpStatus.CREATED;
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error saving package", e);
        }
    }

    @GetMapping("/{packageId}")
    public PackageInformationResponse getPackageInformationByPackageId(@PathVariable Long packageId) {
        return service.getPackageInformationByPackageId(packageId);
    }

    @GetMapping("/locate/{verification}")
    public ResponseEntity<LocationDto> getPackageLocationByVerification(
            @PathVariable String verification) {
        return ResponseEntity.ok(service.findLocationByVerification(verification));
    }

//    @PostMapping("/{packageId}/send-verification")
//    public ResponseEntity<String> sendVerification(@PathVariable String packageId) {
//        String recipient = "551252@student.fontys.nl";
//
//        boolean success = emailService.sendVerificationEmail(recipient, packageId);
//
//        if (success) {
//            return ResponseEntity.ok("Email sent successfully");
//        } else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Failed to send email. Check server logs.");
//        }
//    }
//

    @GetMapping("/verify")
    public PackageInformationResponse getPackageInformationByVerificationCode(@RequestParam String verification) {
        return service.getPackageInformationByVerificationCode(verification);
    }

    @GetMapping("/rooms/{roomId}")
    public List<LocationCountDto> getPackageCountByRoom(@PathVariable Long roomId) {
        return service.getPackageCountPerLocation(roomId);
    }

    @PutMapping("/pickup/{verification}")
    public ResponseEntity<String> pickupPackage(@PathVariable String verification) {
        try {
            service.markPackagePickedUp(verification);

            return ResponseEntity.ok("Package picked up successfully");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error picking up package", e);
        }
    }

    @DeleteMapping("/location/{verification}")
    public ResponseEntity<Void> deletePackageLocation(
            @PathVariable String verification) {

        service.deletePackageLocation(verification);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public List<PackagesDto> getAllPackages() {
        return service.getAllPackages();
    }

    @GetMapping("/locations")
    public List<LocationDto> getAllLocations() {
        return service.getAllLocations();
    }

    @GetMapping("/information")
    public List<PackageInformationDto> getAllPackageInformation() {
        return service.getAllPackageInformation();
    }

    @GetMapping("/location/{locationId}")
    public ResponseEntity<LocationDto> getLocationById(@PathVariable long locationId) {
        LocationDto loc = service.getLocationById(locationId);
        return ResponseEntity.ok(loc);
    }
}
