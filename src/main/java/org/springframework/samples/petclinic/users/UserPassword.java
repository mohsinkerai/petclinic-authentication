package org.springframework.samples.petclinic.users;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class UserPassword {

    @NotNull
    @Size(min=6)
    private String password;
}
