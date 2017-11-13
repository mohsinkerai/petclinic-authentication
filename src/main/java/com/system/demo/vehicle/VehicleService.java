package com.system.demo.vehicle;

import com.google.common.collect.Lists;
import com.opencsv.CSVWriter;
import com.system.demo.volunteer.Volunteer;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
        return new PageImpl<Vehicle>(Lists.newArrayList());
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

    public File exportCsv(VehicleSearchDTO searchDTO) throws IOException {
        long currentMillis = System.currentTimeMillis();

        List<Vehicle> volunteers = this.advancedSearch(searchDTO);
        CSVWriter writer = new CSVWriter(new FileWriter(String.valueOf(currentMillis) + "-vehicle-export.csv"));
        writer.writeNext(headers());
        List<String[]> listOfVolunteer = volunteers.stream().map(this::map)
            .collect(Collectors.toList());
        writer.writeAll(listOfVolunteer);
        writer.close();
        return new File(String.valueOf(currentMillis) + "-vehicle-export.csv");
    }

    public List<Vehicle> advancedSearch(VehicleSearchDTO query) {
        return vehicleRepository
            .findByDriverNameContainingIgnoreCaseAndDriverCnicContainingIgnoreCaseAndLicenseContainingIgnoreCaseAndRegistrationContainingIgnoreCaseAndMakeContainingIgnoreCaseAndColorContainingIgnoreCase(
                query.getDriverName(),
                query.getDriverCnic(),
                query.getLicense(),
                query.getRegistration(),
                query.getMake(),
                query.getColor());
    }

    public Page<Vehicle> advancedSearch(VehicleSearchDTO query, Pageable pageable) {
        return vehicleRepository
            .findByDriverNameContainingIgnoreCaseAndDriverCnicContainingIgnoreCaseAndLicenseContainingIgnoreCaseAndRegistrationContainingIgnoreCaseAndMakeContainingIgnoreCaseAndColorContainingIgnoreCase(
                query.getDriverName(),
                query.getDriverCnic(),
                query.getLicense(),
                query.getRegistration(),
                query.getMake(),
                query.getColor(),
                pageable);
    }

    private String[] headers() {
        return new String[]{
            "Name",
            "Registration",
            "Make",
            "Model",
            "Color"
        };
    }

    private String[] map(Vehicle vehicle) {
        return new String[]{
            vehicle.getDriverName(),
            vehicle.getRegistration(),
            vehicle.getMake(),
            vehicle.getModel(),
            vehicle.getColor()
        };
    }
}
