package com.system.demo.bulk.vehicle;

import com.system.demo.vehicle.Vehicle;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

/**
 * Created by Zeeshan Damani
 */
public class VehicleFieldSetMapper implements FieldSetMapper<Vehicle> {
    public Vehicle mapFieldSet(FieldSet fieldSet) {

    Vehicle vehicle = new Vehicle();
    vehicle.setOwnerName(fieldSet.readString(0));
    vehicle.setOwnerCurrentAddress(fieldSet.readString(1));
    vehicle.setOwnerMobile(fieldSet.readString(2));
    vehicle.setOwnerDriver(fieldSet.readBoolean(3));
    return vehicle;
    }
}
