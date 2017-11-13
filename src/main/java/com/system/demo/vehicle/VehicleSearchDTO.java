package com.system.demo.vehicle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleSearchDTO {

    private String driverName;
    private String driverCnic;
    private String license;
    private String registration;
    private String make;
    private String color;

    public boolean isEmpty() {
        return isNull(driverName) &&
            isNull(driverCnic) &&
            isNull(license) &&
            isNull(registration) &&
            isNull(make) &&
            isNull(color);
    }

    private boolean isNull(Object obj) {
        return obj == null;
    }
}
