/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.samples.petclinic.users.MyUser;
import org.springframework.samples.petclinic.users.UserRepository;
import org.springframework.samples.petclinic.vehicle.Vehicle;
import org.springframework.samples.petclinic.vehicle.VehicleRepository;
import org.springframework.stereotype.Component;

/**
 * PetClinic Spring Boot Application.
 *
 * @author Dave Syer
 */
@SpringBootApplication
@Component
@Slf4j
public class PetClinicApplication implements CommandLineRunner {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    public PetClinicApplication(
        UserRepository userRepository,
        VehicleRepository vehicleRepository) {
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(PetClinicApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        MyUser user = MyUser.builder()
            .authority("ADMIN")
            .isExpired(false)
            .isLocked(false)
            .enabled(true)
            .username("foo")
            .password("bar123")
            .build();

        MyUser user2 = MyUser.builder()
            .authority("ADMIN,USER")
            .isExpired(false)
            .isLocked(false)
            .enabled(true)
            .username("admin")
            .password("123123")
            .build();

        log.info("User 1 is {} and user 2 is {}", user, user2);

        userRepository.save(user);
        userRepository.save(user2);

        String[] types = new String[]{
            "Toyota Corolla GLI",
            "Suzuki Mehran",
            "Toyota Hilux"
        };

        String[] colors = new String[]{
            "White",
            "Green",
            "Black",
            "Grey"
        };

        String[] names = new String[]{
            "Naveed Tejani",
            "Alishah Sayani",
            "Zeeshan Damani",
            "Mohsin Kerai"
        };

        for (int i = 0; i < 41; i++) {
            Vehicle vehicle = new Vehicle();
            vehicle.setCarModel(types[RandomUtils.nextInt(0, types.length)]);
            vehicle.setDriverName(names[RandomUtils.nextInt(0, names.length)]);
            vehicle.setCarRegistrationNumber("ABC-" + RandomUtils.nextInt(100, 999));
            vehicle.setCarColor(colors[RandomUtils.nextInt(0, colors.length)]);
            vehicleRepository.save(vehicle);
        }
    }
}
