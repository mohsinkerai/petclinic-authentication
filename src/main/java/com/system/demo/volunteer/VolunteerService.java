package com.system.demo.volunteer;

import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
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

    public Volunteer findOne(long id) {
        return volunteerRepository.findOne(id);
    }

    public Volunteer save(Volunteer volunteer) {
        return volunteerRepository.save(volunteer);
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
            .findByVolunteerNameContainingIgnoreCaseAndVolunteerCnicContainingAndJamatKhannaContainingIgnoreCaseAndCategory(
                query.getName(),
                query.getCnic(),
                query.getJamatkhana(),
                query.getCategory(),
                pageable);
    }

    public List<Volunteer> advancedSearch(VolunteerSearchDTO query) {
        log.info("Hello World {}", query);
        return volunteerRepository
            .findByVolunteerNameContainingIgnoreCaseAndVolunteerCnicContainingAndJamatKhannaContainingIgnoreCaseAndCategory(
                query.getName(),
                query.getCnic(),
                query.getJamatkhana(),
                query.getCategory());
    }

//    public void  updateVolunteer(Volunteer v){ volunteerRepository.UpdateVolunteer(v);}

    public void delete(Long id) {
        volunteerRepository.delete(id);
    }

    public List<Volunteer> findByCnic(String nic) {
        return volunteerRepository.findByVolunteerCnic(nic);
    }

    public File exportCsv(VolunteerSearchDTO query) throws IOException {
        List<Volunteer> volunteers = this.advancedSearch(query);
        CSVWriter writer = new CSVWriter(new FileWriter("hello.csv"));
        writer.writeNext(headers());
        List<String[]> listOfVolunteer = volunteers.stream().map(this::map)
            .collect(Collectors.toList());
        writer.writeAll(listOfVolunteer);
        writer.close();
        return new File("hello.csv");
    }

    private String[] headers() {
        return new String[]{
            "Name",
            "Jamatkhana",
            "CNIC",
            "EmailAddress"
        };
    }

    private String[] map(Volunteer volunteer) {
        return new String[]{
            volunteer.getVolunteerName(),
            volunteer.getJamatKhanna(),
            volunteer.getVolunteerCnic(),
            volunteer.getEmailAdddress()
        };
    }
}
