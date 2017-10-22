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
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.samples.petclinic.users.MyUser;
import org.springframework.samples.petclinic.users.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * PetClinic Spring Boot Application.
 *
 * @author Dave Syer
 *
 */
@SpringBootApplication
@Component
@Slf4j
public class PetClinicApplication implements CommandLineRunner{

    private final UserRepository userRepository;

    public PetClinicApplication(
        UserRepository userRepository) {
        this.userRepository = userRepository;
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
            .password("bar")
            .build();

        MyUser user2 = MyUser.builder()
            .authority("ADMIN,USER")
            .isExpired(false)
            .isLocked(false)
            .enabled(true)
            .username("admin")
            .password("123")
            .build();

        log.info("User 1 is {} and user 2 is {}",user, user2);

        userRepository.save(user);
        userRepository.save(user2);
    }
}
