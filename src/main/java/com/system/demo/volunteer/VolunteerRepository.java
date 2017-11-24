package com.system.demo.volunteer;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by Zeeshan Damani
 */
@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {

    Page<Volunteer> findByVolunteerNameContainingIgnoreCaseAndVolunteerCnicContainingAndLocalCouncilContainingIgnoreCaseAndDutyZoneContainingIgnoreCase(
        String VolunteerName,
        String VolunteerCnic,
        String LocalCouncil,
        String zone,
        Pageable page);

    List<Volunteer> findByVolunteerNameContainingIgnoreCaseAndVolunteerCnicContainingAndLocalCouncilContainingIgnoreCaseAndDutyZoneContainingIgnoreCase(
        String VolunteerName,
        String VolunteerCnic,
        String LocalCouncil,
        String zone);

    List<Volunteer> findByVolunteerCnic(String VolunteerCnic);

    @Query(value = "SELECT count(id) FROM volunteers WHERE volunteer_isprinted = 0", nativeQuery = true)
    long getUnprintedCount();

    @Query(value = "SELECT count(id) FROM volunteers WHERE volunteer_isprinted = 0 AND isPictureAvailable = 1 AND volunteer_image is not null AND "
        + " volunteer_name is not null AND"
        + " volunteer_committee is not null AND"
        + " volunteer_site is not null AND"
        + " local_council is not null AND"
        + " volunteer_duty_zone is not null", nativeQuery = true)
    long getUnprintedClearCount();

    Page<Volunteer> findByVolunteerIsPrintedIsFalse(Pageable pageable);
}
