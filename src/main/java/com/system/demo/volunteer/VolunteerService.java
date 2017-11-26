package com.system.demo.volunteer;

import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
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
    private final VolunteerImageService volunteerImageService;

    @Autowired
    public VolunteerService(VolunteerRepository volunteerRepository,
        VolunteerImageService volunteerImageService) {
        this.volunteerRepository = volunteerRepository;
        this.volunteerImageService = volunteerImageService;
    }

    public Volunteer findOne(long id) {
        Volunteer volunteer = volunteerRepository.findOne(id);
        try {
            String imageBase64 = volunteerImageService.read(volunteer.getVolunteerImage());
            volunteer.setPicture(imageBase64);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return volunteer;
    }

    public Volunteer findOneWithoutImage(long id) {
        return volunteerRepository.findOne(id);
    }

    public Volunteer save(Volunteer volunteer) {
        if (volunteer.getPicture() != null && volunteer.getPicture().trim() != "") {
            String filePath = null;
            try {
                filePath = volunteerImageService
                    .write(volunteer.getPicture(), volunteer.getVolunteerCnic());
                volunteer.setVolunteerImage(filePath);
                if (filePath != null) {
                    volunteer.setPictureAvailable(true);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return volunteerRepository.save(volunteer);
    }

    public Page<Volunteer> findAllUnPrinted(Pageable pageable) {
        return volunteerRepository.findByVolunteerIsPrintedIsFalse(pageable);

        //        return volunteerRepository.findAll(pageable);
    }

    public Page<Volunteer> search(String query, Pageable pageable) {
        return volunteerRepository.findAll(pageable);
    }

    public Page<Volunteer> advancedSearch(VolunteerSearchDTO query, Pageable pageable) {
        Page<Volunteer> searched = volunteerRepository
            .findByVolunteerNameContainingIgnoreCaseAndVolunteerCnicContainingAndLocalCouncilContainingIgnoreCaseAndDutyZoneContainingIgnoreCase(
                Optional.ofNullable(query.getName()).orElse(""),
                Optional.ofNullable(query.getCnic()).orElse(""),
                Optional.ofNullable(query.getLocalCouncil()).orElse(""),
                Optional.ofNullable(query.getZone()).orElse(""),
                pageable);
        return searched;
    }

    public List<Volunteer> advancedSearch(VolunteerSearchDTO query) {
        return volunteerRepository
            .findByVolunteerNameContainingIgnoreCaseAndVolunteerCnicContainingAndLocalCouncilContainingIgnoreCaseAndDutyZoneContainingIgnoreCase(
                Optional.ofNullable(query.getName()).orElse(""),
                Optional.ofNullable(query.getCnic()).orElse(""),
                Optional.ofNullable(query.getLocalCouncil()).orElse(""),
                Optional.ofNullable(query.getZone()).orElse(""));
    }

//    public void  updateVolunteer(Volunteer v){ volunteerRepository.UpdateVolunteer(v);}

    public List<Volunteer> findPrintableVolunteers() {
        List<Volunteer> volunteers = volunteerRepository.findAll();
        return filterForPrinting(volunteers);
    }

    public List<Volunteer> findPrintableVolunteers(VolunteerSearchDTO query) {
        return filterForPrinting(advancedSearch(query));
    }

    private List<Volunteer> filterForPrinting(List<Volunteer> volunteers) {
        return volunteers.stream()
            .filter(volunteer -> !volunteer.isVolunteerIsPrinted())
            .filter(Volunteer::isValidForPrint)
            .collect(Collectors.toList());
    }

    public Volunteer createNew() {
        return new Volunteer();
    }

    public long getUnprintedCount() {
        return volunteerRepository.getUnprintedCount();
    }

    public long getToBePrintedCount() {
        return volunteerRepository.getUnprintedClearCount();
    }

    public void delete(Long id) {
        volunteerRepository.delete(id);
    }

    public List<Volunteer> findByCnic(String nic) {
        return volunteerRepository.findByVolunteerCnic(nic);
    }

    public File exportCsv(VolunteerSearchDTO query) throws IOException {
        String currentMillis = String.valueOf(System.currentTimeMillis());
        List<Volunteer> volunteers = this.advancedSearch(query);
        CSVWriter writer = new CSVWriter(new FileWriter(currentMillis+"hello.csv"));
        writer.writeNext(headers());
        List<String[]> listOfVolunteer = volunteers.stream().map(this::map)
            .collect(Collectors.toList());
        writer.writeAll(listOfVolunteer);
        writer.close();
        return new File(currentMillis+"hello.csv");
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
            Optional.ofNullable(volunteer.getVolunteerName()).orElse(""),
            Optional.ofNullable(volunteer.getJamatKhanna()).orElse(""),
            Optional.ofNullable(volunteer.getVolunteerCnic()).orElse(""),
            Optional.ofNullable(volunteer.getEmailAdddress()).orElse("")
        };
    }
}
