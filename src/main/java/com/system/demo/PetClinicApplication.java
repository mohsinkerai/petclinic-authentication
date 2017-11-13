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

import static com.system.demo.users.UserAuthority.ADMIN;
import static com.system.demo.users.UserAuthority.AUTHORIZER;
import static com.system.demo.users.UserAuthority.CARDISSUER;
import static com.system.demo.users.UserAuthority.REGISTRAR;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.system.demo.users.MyUser;
import com.system.demo.users.UserRepository;
import com.system.demo.vehicle.Vehicle;
import com.system.demo.vehicle.VehicleRepository;
import com.system.demo.volunteer.Volunteer;
import com.system.demo.volunteer.VolunteerCategory;
import com.system.demo.volunteer.VolunteerRepository;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

/**
 * PetClinic Spring Boot Application.
 *
 * @author Dave Syer
 */
@SpringBootApplication
@Component
@Slf4j
@EnableAsync
public class PetClinicApplication implements CommandLineRunner {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final VolunteerRepository volunteerRepository;

    private RandomStringGenerator stringGenerator = new RandomStringGenerator.Builder()
        .withinRange('a', 'z').build();

    public PetClinicApplication(
        UserRepository userRepository,
        VehicleRepository vehicleRepository,
        VolunteerRepository volunteerRepository) {
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
        this.volunteerRepository = volunteerRepository;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(PetClinicApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        try {
            PopulateData();
        } catch (Exception ex) {
            log.info("Exception in Populating Data", ex);
        }
    }

    private void PopulateData() {
        MyUser user = MyUser.builder()
//            .authority(Lists.newArrayList(
//                new MyUserAuthority("ADMIN"),
//                new MyUserAuthority("REGISTRAR"),
//                new MyUserAuthority("AUTHORIZER"),
//                new MyUserAuthority("CARDISSUER")
//            ))
            .authority(Lists.newArrayList(
                ADMIN,
                REGISTRAR,
                AUTHORIZER,
                CARDISSUER
            ))
            .isExpired(false)
            .isLocked(false)
            .enabled(true)
            .username("foo")
            .password("bar123")
            .build();

        MyUser user2 = MyUser.builder()
            .authority(Lists.newArrayList(
                ADMIN,
                REGISTRAR,
                AUTHORIZER,
                CARDISSUER
            ))
            .isExpired(false)
            .isLocked(false)
            .enabled(true)
            .username("admin")
            .password("123123")
            .build();

        MyUser user3 = MyUser.builder()
            .authority(
                ImmutableList.of(
                    REGISTRAR
                )
            )
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

        String[] names = new String[]{
            "Naveed Tejani",
            "Alishah Jaffer Sayani",
            "Zeeshan Moiz Damani",
            "Mohsin Mansoor Kerai"
        };

        for (int i = 0; i < 25; i++) {
            Vehicle vehicle = new Vehicle();
            vehicle.setMake(make[RandomUtils.nextInt(0, make.length)]);
            vehicle.setModel(model[RandomUtils.nextInt(0, model.length)]);
            vehicle.setColor(colors[RandomUtils.nextInt(0, colors.length)]);

            int nameIndex = RandomUtils.nextInt(0, names.length);

            vehicle.setDriverName(names[nameIndex]);
            vehicle.setOwnerName(names[nameIndex]);
            vehicle.setOwnerCnic(String.valueOf(RandomUtils.nextInt(99999, 999999)));
            vehicle.setDriverCnic(String.valueOf(RandomUtils.nextInt(99999, 999999)));
            vehicle.setOwnerMobile(String.valueOf(RandomUtils.nextInt(99999, 999999)));
            vehicle.setDriverMobile(String.valueOf(RandomUtils.nextInt(99999, 999999)));
            vehicle.setOwnerCurrentAddress("Dilkusha Forum");
            vehicle.setDriverCurrentAddress("Karimabad Colony");

            vehicle.setLicense(String.valueOf(RandomUtils.nextInt(99999, 999999)));
            vehicle.setLicenseIssue(new Date());
            vehicle.setLicenseExpiry(new Date());

            vehicle.setRegistration("APM-" + RandomUtils.nextInt(100, 999));
            vehicle.setChassisNumber(String.valueOf(RandomUtils.nextInt(9999, 99999)));
            vehicle.setEngineNumber(String.valueOf(RandomUtils.nextInt(9999, 99999)));
            vehicle.setEnabled(true);
            vehicle.setOwnerDriver(false);
            vehicleRepository.save(vehicle);
        }

        Volunteer volunteer = Volunteer
            .builder()
            .volunteerName("Zeeshan Damani")
            .volunteerCnic("1234")
            .age("98")
            .jamatKhanna("Karimabad")
            .category(VolunteerCategory.GOLD)
            .build();

        volunteerRepository.save(volunteer);

        volunteer = Volunteer
            .builder()
            .volunteerName("Mohsin Kerai")
            .volunteerCnic("9876")
            .jamatKhanna("Alyabad")
            .age("97")
            .category(VolunteerCategory.GOLD)
            .build();

        volunteerRepository.save(volunteer);
    }
}
