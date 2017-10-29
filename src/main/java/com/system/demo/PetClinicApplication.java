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

package com.system.demo;

import com.system.demo.users.UserRepository;
import com.system.demo.vehicle.VehicleType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.system.demo.users.MyUser;
import com.system.demo.vehicle.Vehicle;
import com.system.demo.vehicle.VehicleRepository;
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

    private RandomStringGenerator stringGenerator = new RandomStringGenerator.Builder().withinRange('a','z').build();

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
            .authority("ADMIN,REGISTRAR")
            .isExpired(false)
            .isLocked(false)
            .enabled(true)
            .username("foo")
            .password("bar123")
            .build();

        MyUser user2 = MyUser.builder()
            .authority("ADMIN,REGISTRAR")
            .isExpired(false)
            .isLocked(false)
            .enabled(true)
            .username("admin")
            .password("123123")
            .build();

        MyUser user3 = MyUser.builder()
            .authority("REGISTRAR")
            .isExpired(false)
            .isLocked(false)
            .enabled(true)
            .username("registrar")
            .password("registrar")
            .build();

        log.info("User 1 is {} and user 2 is {}", user, user2);

        userRepository.save(user);
        userRepository.save(user2);
        userRepository.save(user3);

        String[] make = new String[]{
            "Suzuki",
            "Toyota",
            "Honda"
        };

        String[] model = new String[]{
            "Corolla 2008",
            "Alto 2007",
            "Mehran 1991",
            "Accord 2017"
        };

        String[] colors = new String[]{
            "White",
            "Green",
            "Black",
            "Grey"
        };

        String[][] names = new String[][]{
            {"Naveed", "", "Tejani"},
            {"Alishah", "Jaffer", "Sayani"},
            {"Zeeshan", "Moiz", "Damani"},
            {"Mohsin", "Mansoor", "Kerai"}
        };

        for (int i = 0; i < 41; i++) {
            Vehicle vehicle = new Vehicle();
            vehicle.setMake(make[RandomUtils.nextInt(0, make.length)]);
            vehicle.setModel(model[RandomUtils.nextInt(0, model.length)]);
            vehicle.setColor(colors[RandomUtils.nextInt(0, colors.length)]);
            int nameIndex = RandomUtils.nextInt(0, names.length);
            vehicle.setFirstname(names[nameIndex][0]);
            vehicle.setFathername(names[nameIndex][1]);
            vehicle.setSurname(names[nameIndex][2]);
            vehicle.setCnic(stringGenerator.generate(10));
            vehicle.setRegistration("APM-" + RandomUtils.nextInt(100, 999));
            vehicle.setEnabled(true);
            vehicle.setCategory(VehicleType.FOURX4);
            vehicleRepository.save(vehicle);
        }
    }
}
