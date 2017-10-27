package org.springframework.samples.petclinic.vehicle;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "vehicle")
@ToString
@EqualsAndHashCode
@Setter
@Getter
@NoArgsConstructor
public class Vehicle implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    private String carRegistrationNumber;
    private String driverName;
    private String carModel;
    private String carColor;
    private boolean enabled;

    public boolean isNew() {
        return this.id == null;
    }
}
