package com.system.demo.volunteer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Created by Zeeshan Damani
 */
@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {

    Page<Volunteer> findByVolunteerNameContainingIgnoreCaseAndVolunteerCnicContainingAndJamatKhannaContainingIgnoreCaseAndDutyZoneContainingIgnoreCase(
        String VolunteerName,
        String VolunteerCnic,
        String JamatKhanna,
        String zone,
        Pageable page);

    List<Volunteer> findByVolunteerNameContainingIgnoreCaseAndVolunteerCnicContainingAndJamatKhannaContainingIgnoreCaseAndDutyZoneContainingIgnoreCase(
        String VolunteerName,
        String VolunteerCnic,
        String JamatKhanna,
        String zone);

    List<Volunteer> findByVolunteerCnic(String VolunteerCnic);

   // void UpdateVolunteer(Volunteer v);




}
