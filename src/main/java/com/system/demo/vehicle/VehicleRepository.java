package com.system.demo.vehicle;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Page<Vehicle> findByEnabledTrue(Pageable pageable);

    Page<Vehicle> findByDriverNameContainingIgnoreCaseAndDriverCnicContainingIgnoreCaseAndLicenseContainingIgnoreCaseAndRegistrationContainingIgnoreCaseAndMakeContainingIgnoreCaseAndColorContainingIgnoreCase(
        String DriverName,
        String DriverCnic,
        String License,
        String Registration,
        String Make,
        String Color,
        Pageable pageable);

    List<Vehicle> findByDriverNameContainingIgnoreCaseAndDriverCnicContainingIgnoreCaseAndLicenseContainingIgnoreCaseAndRegistrationContainingIgnoreCaseAndMakeContainingIgnoreCaseAndColorContainingIgnoreCase(
        String DriverName,
        String DriverCnic,
        String License,
        String Registration,
        String Make,
        String Color);
}
