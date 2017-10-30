package com.system.demo.volunteer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
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

    public Volunteer findOne(Long id) {
        return volunteerRepository.findOne(id);
    }

    public void delete(Long id) {
        volunteerRepository.delete(id);
    }
}
