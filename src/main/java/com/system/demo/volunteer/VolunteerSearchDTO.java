package com.system.demo.volunteer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerSearchDTO {

    private String name;
    private String cnic;
    private VolunteerCategory category;
    private String regionalCouncil;
    private String jamatkhana;
    private String localCouncil;
    private String day;
    private String shift;
    private String zone;

    public boolean isEmpty() {
        return isNull(name) &&
            isNull(cnic) &&
            isNull(category) &&
            isNull(localCouncil) &&
            isNull(regionalCouncil) &&
            isNull(jamatkhana) &&
            isNull(day) &&
            isNull(shift) &&
            isNull(zone);
    }

    private boolean isNull(Object obj) {
        return obj == null;
    }
}
