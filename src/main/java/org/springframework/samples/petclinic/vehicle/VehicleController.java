package org.springframework.samples.petclinic.vehicle;

import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.model.SearchDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@Controller
@RequestMapping(VehicleController.BASE_URL)
public class VehicleController {

    public static final String BASE_URL = "vehicle";

    private final VehicleRepository vehicleRepository;
    private static final String VIEW_ALL = "vehicle/list";
    private static final String VIEWS_VEHICLE_CREATE_OR_UPDATE_FORM = "vehicle/createOrUpdateVehicleForm";
    private static final String VIEWS_VEHICLE_DELETE_CONFIRMATION = "vehicle/delete";

    @Autowired
    public VehicleController(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String processFindForm(Pageable pageable,
        Map<String, Object> model, SearchDTO searchDTO) {
        Page<Vehicle> vehicles;
        if (searchDTO.getQuery() == null) {
            vehicles = vehicleRepository.findByEnabledTrue(pageable);
            searchDTO = new SearchDTO();
        } else {
            vehicles = vehicleRepository.findByEnabledTrue(pageable);
        }
        model.put("query", searchDTO);
        model.put("page", vehicles);
        return VIEW_ALL;
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String initCreationForm(Map<String, Object> model) {
        Vehicle vehicle = new Vehicle();
        model.put("vehicle", vehicle);
        return VIEWS_VEHICLE_CREATE_OR_UPDATE_FORM;
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String processCreationForm(@Valid Vehicle vehicle, BindingResult result) {
        if (result.hasErrors()) {
            return VIEWS_VEHICLE_CREATE_OR_UPDATE_FORM;
        } else {
            vehicle.setEnabled(true);
            this.vehicleRepository.save(vehicle);
            return "redirect:/" + BASE_URL;
        }
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        Vehicle repositoryUser = vehicleRepository.findOne(id);
        if (repositoryUser != null) {
            model.addAttribute("vehicle", repositoryUser);
            return VIEWS_VEHICLE_CREATE_OR_UPDATE_FORM;
        } else {
            throw new RuntimeException("Invalid User ID" + id);
        }
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public String editSave(@PathVariable("id") Long id, @Valid Vehicle vehicle,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return VIEWS_VEHICLE_CREATE_OR_UPDATE_FORM;
        } else {
            Vehicle vehicleRepository = this.vehicleRepository.findOne(id);
            vehicleRepository.setCarColor(vehicle.getCarColor());
            vehicleRepository.setCarModel(vehicle.getCarModel());
            vehicleRepository.setCarRegistrationNumber(vehicle.getCarRegistrationNumber());
            vehicleRepository.setDriverName(vehicle.getDriverName());
            this.vehicleRepository.save(vehicleRepository);
            if (vehicleRepository != null) {
                return "redirect:/" + BASE_URL;
            } else {
                throw new RuntimeException("Invalid User ID" + id);
            }
        }
    }

    @RequestMapping(value = "/delete/{id}")
    public String delete(@PathVariable("id") Long id, Model model) {
        Vehicle repositoryUser = vehicleRepository.findOne(id);
        if (repositoryUser != null) {
            model.addAttribute("user", repositoryUser);
            return VIEWS_VEHICLE_DELETE_CONFIRMATION;
        } else {
            throw new RuntimeException("Invalid User ID" + id);
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public String disable(@PathVariable("id") Long id) {
        Vehicle repositoryUser = vehicleRepository.findOne(id);
        if (repositoryUser != null) {
            repositoryUser.setEnabled(false);
            vehicleRepository.save(repositoryUser);
            return "redirect:/" + BASE_URL;
        } else {
            throw new RuntimeException("Invalid User ID" + id);
        }
    }
}
