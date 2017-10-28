package org.springframework.samples.petclinic.vehicle;

public enum VehicleType {
    CAR("Car"),
    HI_ROOF("Hi-Roof"),
    JEEP("Jeep"),
    HILUX("Hilux"),
    COASTER("Coaster"),
    FOURX4("4x4");

    private final String displayName;

    VehicleType(String displayName) {
        this.displayName = displayName;
    }
}
