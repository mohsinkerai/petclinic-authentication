package com.system.demo.vehicle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Page<Vehicle> search(String query, Pageable pageable) {
        return vehicleRepository.findByFirstnameContainingIgnoreCase(query, pageable);
    }

    public Page<Vehicle> findAll(Pageable pageable) {
        return vehicleRepository.findByEnabledTrue(pageable);
    }

    public Vehicle findOne(long key) {
        return vehicleRepository.findOne(key);
    }

    public Vehicle createNew() {
        return new Vehicle();
    }

    public Vehicle save(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }
}
