package com.system.demo.vehicle;

import com.google.common.collect.Lists;
import com.system.demo.model.SearchDTO;
import com.system.demo.users.UserAuthority;
import com.system.demo.volunteer.Volunteer;
import com.system.demo.volunteer.VolunteerSearchDTO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@Controller
@RequestMapping(VehicleController.BASE_URL)
@PreAuthorize("hasAuthority('VEHICLE_REGISTRAR')")
public class VehicleController {

    public static final String BASE_URL = "vehicle";

    private final VehicleService vehicleService;

    private static final String VIEW_ALL = "vehicle/list";
    private static final String SEARCH = "vehicle/search";
    private static final String VIEWS_VEHICLE_CREATE_OR_UPDATE_FORM = "vehicle/createOrUpdateVehicleForm";
    private static final String VIEWS_VEHICLE_DELETE_CONFIRMATION = "vehicle/delete";

    @Autowired
    public VehicleController(
        VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String processFindForm(Pageable pageable,
        Map<String, Object> model, SearchDTO searchDTO) {
        Page<Vehicle> vehicles;
        if (searchDTO == null || searchDTO.getQuery() == null) {
            vehicles = vehicleService.findAll(pageable);
            searchDTO = new SearchDTO();
        } else {
            vehicles = vehicleService.search(searchDTO.getQuery(), pageable);
        }
        model.put("query", searchDTO);
        model.put("page", vehicles);
        return VIEW_ALL;
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String initCreationForm(Map<String, Object> model) {
        Vehicle vehicle = vehicleService.createNew();
        model.put("vehicle", vehicle);
        return VIEWS_VEHICLE_CREATE_OR_UPDATE_FORM;
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String processCreationForm(@Valid Vehicle vehicle, BindingResult result) {
        if (result.hasErrors()) {
            return VIEWS_VEHICLE_CREATE_OR_UPDATE_FORM;
        } else {
            vehicle.setEnabled(true);
            vehicleService.save(vehicle);
            return "redirect:/" + BASE_URL;
        }
    }

    @RequestMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('VEHICLE_AUTHORIZER')")
    public String edit(@PathVariable("id") Long id, Model model) {
        Vehicle vehicle = vehicleService.findOne(id);
        if (vehicle != null && vehicle.isEnabled()) {
            model.addAttribute("vehicle", vehicle);
            return VIEWS_VEHICLE_CREATE_OR_UPDATE_FORM;
        } else {
            throw new RuntimeException("Invalid User ID" + id);
        }
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('VEHICLE_AUTHORIZER')")
    public String editSave(@PathVariable("id") Long id, @Valid Vehicle vehicle,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return VIEWS_VEHICLE_CREATE_OR_UPDATE_FORM;
        } else {
            Vehicle vehicleRepository = vehicleService.findOne(id);
            if (vehicleRepository == null) {
                throw new RuntimeException("Invalid User ID" + id);
            }
            vehicle.setId(vehicleRepository.getId());
            vehicle.setEnabled(vehicleRepository.isEnabled());
            vehicleService.save(vehicle);
            return "redirect:/" + BASE_URL;
        }
    }

    @RequestMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('VEHICLE_AUTHORIZER')")
    public String delete(@PathVariable("id") Long id, Model model) {
        Vehicle repositoryUser = vehicleService.findOne(id);
        if (repositoryUser != null) {
            model.addAttribute("vehicle", repositoryUser);
            return VIEWS_VEHICLE_DELETE_CONFIRMATION;
        } else {
            throw new RuntimeException("Invalid User ID" + id);
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('VEHICLE_AUTHORIZER')")
    public String disable(@PathVariable("id") Long id) {
        Vehicle vehicle = vehicleService.findOne(id);
        if (vehicle != null) {
            vehicle.setEnabled(false);
            vehicleService.save(vehicle);
            return "redirect:/" + BASE_URL;
        } else {
            throw new RuntimeException("Invalid User ID" + id);
        }
    }

    @RequestMapping(path = "/search/export", method = RequestMethod.GET)
    public void export(VehicleSearchDTO searchDTO, HttpServletResponse response)
        throws IOException {
        File file = vehicleService.exportCsv(searchDTO);

        response.setContentLength((int) file.length());
        InputStream inputStream = new FileInputStream(file);
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + "hello.csv" + "\"");
        FileCopyUtils.copy(inputStream, response.getOutputStream());
        response.flushBuffer();
        inputStream.close();
    }

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public String search(Pageable pageable,
        Map<String, Object> model, VehicleSearchDTO searchDTO) {
        Page<Vehicle> volunteers;
        if (searchDTO.isEmpty()) {
            searchDTO = new VehicleSearchDTO();
            volunteers = new PageImpl<Vehicle>(Lists.newArrayList());
        } else {
            volunteers = vehicleService.advancedSearch(searchDTO, pageable);
        }
        model.put("query", searchDTO);
        model.put("page", volunteers);
        return SEARCH;
    }
}
