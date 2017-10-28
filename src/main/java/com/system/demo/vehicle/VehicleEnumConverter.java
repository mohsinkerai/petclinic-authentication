package com.system.demo.vehicle;

import org.springframework.core.convert.converter.Converter;

public class VehicleEnumConverter implements Converter<String, VehicleType> {

    @Override
    public VehicleType convert(String s) {
        return VehicleType.valueOf(s);
    }
}
