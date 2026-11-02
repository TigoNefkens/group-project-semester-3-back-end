# Quality Plan
**Project:** group-2-sioux-back-end  
**Date:** January 2026

---

## Executive Summary

This document outlines the testing strategy, implementation, and results for the backend service quality assurance initiative.

### Key Achievements

```
Tests Implemented: 37 unit tests
Services Covered:  3/3 main business services
Improvement:       +355% from baseline (11% → 50%)
```

---

## Testing Strategy

### Approach

We implemented a **comprehensive unit testing strategy** using industry-standard tools and best practices:

- **Test Framework:** JUnit 5
- **Mocking Framework:** Mockito
- **Coverage Tool:** JaCoCo
- **Testing Style:** Arrange-Act-Assert

### Scope

The testing effort focused on the **business layer** , which contains the core business logic:

1.  **PackageService** - Package management operations
2. **LocationService** - Location tracking and management
3. **ShelfService** - Shelf and storage operations

---

## Coverage Metrics


```
┌─────────────────────────────────────────┐
│  Service           Coverage   Status    │
├─────────────────────────────────────────┤
│  PackageService      79%      ✅ Excellent │
│  LocationService     74%      ✅ Good      │
│  ShelfService        20%      🟡 Basic     │
├─────────────────────────────────────────┤
│  TOTAL (business)    50%      ✅ Target   │
└─────────────────────────────────────────┘
```


---

## Test Implementation Details

### Test Distribution

| Test Suite | Tests | Coverage Focus |
|------------|-------|----------------|
| **PackageServiceTest** | 22 | CRUD operations, validations, aggregations |
| **ShelfServiceTest** | 12 | Storage management, constraints |
| **LocationServiceTest** | 2 | Location tracking |
| **ApplicationTests** | 1 | Application startup |
| **TOTAL** | **37** | **Comprehensive coverage** |

### PackageServiceTest (22 tests)

**Methods Covered:**
- `createPackage()` - Create new packages with validation
- `findLocationByVerification()` - Locate packages by verification code
- `getAllPackages()` - Retrieve all packages
- `getAllLocations()` - Retrieve all storage locations
- `getAllPackageInformation()` - Get package metadata
- `getPackageInformationByPackageId()` - Query by package ID
- `getLocationById()` - Query location by ID
- `getPackageInformationByVerificationCode()` - Query by verification
- `markPackagePickedUp()` - Update delivery status
- `getPackageCountPerLocation()` - Aggregate package counts

**Test Coverage Highlights:**
- Happy path scenarios 
- Error handling (not found, invalid data)
- Edge cases (empty lists, null values) 
- Business rule validation

### ShelfServiceTest (12 tests)

**Methods Covered:**
- `findShelfByLocation()` - Shelf existence validation
- `getAllShelves()` - Retrieve all shelves
- `getShelvesByRoom()` - Filter shelves by room
- `deleteShelf()` - Shelf deletion with package validation
- `getAllRoomNumbers()` - Get available rooms
- `updateSectionFullStatus()` - Update storage capacity

**Critical Validations Tested:**
- Cannot delete shelf with packages 
- Room existence validation 
- Empty result handling 

### LocationServiceTest (2 tests)

**Methods Covered:**
- `createLocation()` - Create new storage location
- Location validation

---

## Tools & Technologies


### Running Tests



## Evidence & Artifacts

### Test Execution Results

```
> Task : test

PackageServiceTest > 22 tests PASSED 
ShelfServiceTest > 12 tests PASSED 
LocationServiceTest > 2 tests PASSED 

BUILD SUCCESSFUL in 8s
37 tests completed, 37 passed, 0 failed
```

### Coverage Report Location

```
build/
  └── jacocoHtml/
      ├── index.html           
```

---

## Testing Best Practices Applied

### 1. Arrange-Act-Assert

Every test follows a clear structure:

```java
@Test
void createPackage_WhenEverythingIsOk_ShouldSavePackage() {
    // ──── ARRANGE ────
    PackageSaveRequest request = new PackageSaveRequest(1, 101, "A", 2, 3);
    when(shelfService.findShelfByLocation(... )).thenReturn(true);
    
    // ──── ACT ────
    packageService.createPackage(request);
    
    // ──── ASSERT ────
    verify(packageRepository, times(1)).save(any());
}
```

### 2. Descriptive Test Names

```java
// Good:  Clearly states WHAT, WHEN, and EXPECTED
findLocationByVerification_WhenPackageExists_ShouldReturnLocation()
```

### 3. Isolated Tests

- Each test is independent
- Uses mocks to avoid external dependencies
- No shared state between tests

### 4. Comprehensive Coverage

```
Happy path (success scenarios)
Error handling (exceptions, not found)
Edge cases (empty lists, null values)
Business validations (constraints, rules)
```

---

## Example Test Case

### Real Implementation Example

```java
@Test
void markPackagePickedUp_WhenExists_ShouldUpdateStatus() {
    
    // ARRANGE:  Prepare test data
    String verificationCode = "ABC123";
    PackageInformationEntity packageInfo = PackageInformationEntity.builder()
            .id(1)
            .verification(verificationCode)
            .status("STORED")
            .stored(true)
            .build();
    
    when(packageInformationRepository.findByVerification(verificationCode))
            .thenReturn(Optional.of(packageInfo));
    
    // ACT: Execute the method
    packageService.markPackagePickedUp(verificationCode);
    
    // ASSERT: Verify state changes
    verify(packageInformationRepository, times(1)).save(argThat(info ->
            info.getStatus().equals("Delivered") &&
            info.getStored().equals(false) &&
            info.getPickupDate() != null
    ));
}
```

**What this tests:**
- Status changes from "STORED" to "Delivered"
- `stored` flag is set to `false`
- `pickupDate` is populated with current timestamp
- Repository save is called exactly once

---



## Checklist of Achievements

### Implemented 

- Unit test framework setup (JUnit 5 + Mockito)
- Code coverage tool integration (JaCoCo)
- 37 comprehensive unit tests
- 50% code coverage achieved
- Coverage reports generated
- Best practices documented

### Quality Metrics 

- No failing tests
- All critical paths tested
- Error scenarios covered
- Edge cases validated
- Business rules enforced

---



## Conclusion

This testing initiative successfully achieved **50% code coverage** through the implementation of **37 comprehensive unit tests** across the core business layer.

### Key Takeaways

**Solid foundation** for quality assurance  
**Industry-standard tools** and practices  
**Maintainable test suite** with clear patterns  
**Documented approach** for team knowledge sharing  
**Scalable framework** for future expansion
