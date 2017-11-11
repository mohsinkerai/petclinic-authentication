package com.system.demo.volunteer;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Zeeshan Damani
 */
@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {

    Page<Volunteer> findByVolunteerNameContainingIgnoreCaseAndVolunteerCnicContainingAndJamatKhannaContainingIgnoreCaseAndCategory(
        String VolunteerName,
        String VolunteerCnic,
        String JamatKhanna,
        VolunteerCategory category,
        Pageable page);

    List<Volunteer> findByVolunteerNameContainingIgnoreCaseAndVolunteerCnicContainingAndJamatKhannaContainingIgnoreCaseAndCategory(
        String VolunteerName,
        String VolunteerCnic,
        String JamatKhanna,
        VolunteerCategory category);
}
