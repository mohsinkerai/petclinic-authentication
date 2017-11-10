package com.system.demo.volunteer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Zeeshan Damani
 */
@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {

    Page<Volunteer> findByVolunteerNameContainingIgnoreCaseAndVolunteerCnicContainingAndJamatKhannaContainingIgnoreCase(
        String VolunteerName,
        String VolunteerCnic,
        String JamatKhanna,
        Pageable page);
}
