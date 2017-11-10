package com.system.demo.volunteer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;

    @Autowired
    public VolunteerService(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    public Page<Volunteer> findAll(Pageable pageable) {
        return volunteerRepository.findAll(pageable);
    }

    public Page<Volunteer> search(String query, Pageable pageable) {
        return volunteerRepository.findAll(pageable);
    }

    public Page<Volunteer> advancedSearch(VolunteerSearchDTO query, Pageable pageable) {
        log.info("Hello World {}", query);
        return volunteerRepository
            .findByVolunteerNameContainingIgnoreCaseAndVolunteerCnicContainingAndJamatKhannaContainingIgnoreCase(
                query.getName(),
                query.getCnic(),
                query.getJamatkhana(),
                pageable);
    }

    public Volunteer findOne(Long id) {
        return volunteerRepository.findOne(id);
    }

    public void delete(Long id) {
        volunteerRepository.delete(id);
    }
}
