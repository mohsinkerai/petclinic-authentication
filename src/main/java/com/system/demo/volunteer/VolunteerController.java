package com.system.demo.volunteer;

import com.google.common.collect.Lists;
import com.system.demo.bulk.volunteer.StorageService;
import com.system.demo.bulk.volunteer.event.FileUploadEvent;
import com.system.demo.bulkprogress.itemdata.FailItemService;
import com.system.demo.bulkprogress.itemdata.FailItems;
import com.system.demo.bulkprogress.jobdata.UserJobData;
import com.system.demo.bulkprogress.jobdata.UserJobRepository;
import com.system.demo.model.SearchDTO;
import com.system.demo.users.MyUser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@RequestMapping(VolunteerController.BASE_URL)
@PreAuthorize("hasAuthority('REGISTRAR')")
public class VolunteerController {

    public static final String BASE_URL = "volunteer";

    private final ApplicationEventPublisher applicationEventPublisher;
    private final VolunteerService volunteerService;
    private final StorageService storageService;
    private final FailItemService failItemService;
    private final UserJobRepository userJobRepository;

    private static final String VIEW_ALL = "volunteer/list";
    private static final String SEARCH = "volunteer/search";
    private static final String VIEWS_VEHICLE_DELETE_CONFIRMATION = "volunteer/delete";

    @Autowired
    public VolunteerController(
        ApplicationEventPublisher applicationEventPublisher,
        VolunteerService volunteerService,
        StorageService storageService,
        FailItemService failItemService,
        UserJobRepository userJobRepository) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.volunteerService = volunteerService;
        this.storageService = storageService;
        this.failItemService = failItemService;
        this.userJobRepository = userJobRepository;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String processFindForm(Pageable pageable,
        Map<String, Object> model, SearchDTO searchDTO) {
        Page<Volunteer> vehicles;
        if (searchDTO.getQuery() == null) {
            vehicles = volunteerService.findAll(pageable);
            searchDTO = new SearchDTO();
        } else {
            vehicles = volunteerService.search(searchDTO.getQuery(), pageable);
        }
        model.put("query", searchDTO);
        model.put("page", vehicles);
        return VIEW_ALL;
    }

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public String search(Pageable pageable,
        Map<String, Object> model, VolunteerSearchDTO searchDTO) {
        Page<Volunteer> volunteers;
        if (searchDTO.isEmpty()) {
            searchDTO = new VolunteerSearchDTO();
            volunteers = new PageImpl<Volunteer>(Lists.newArrayList());
        } else {
            volunteers = volunteerService.advancedSearch(searchDTO, pageable);
        }
        model.put("query", searchDTO);
        model.put("page", volunteers);
        return SEARCH;
    }

//    @RequestMapping("/edit/{id}")
//    public String edit(@PathVariable("id") Long id, Model model) {
//        Vehicle vehicle = vehicleService.findOne(id);
//        if (vehicle != null && vehicle.isEnabled()) {
//            model.addAttribute("vehicle", vehicle);
//            return VIEWS_VEHICLE_CREATE_OR_UPDATE_FORM;
//        } else {
//            throw new RuntimeException("Invalid User ID" + id);
//        }
//    }
//
//    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
//    public String editSave(@PathVariable("id") Long id, @Valid Vehicle vehicle,
//        BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return VIEWS_VEHICLE_CREATE_OR_UPDATE_FORM;
//        } else {
//            Vehicle vehicleRepository = vehicleService.findOne(id);
//            if (vehicleRepository == null) {
//                throw new RuntimeException("Invalid User ID" + id);
//            }
//            vehicle.setId(vehicleRepository.getId());
//            vehicle.setEnabled(vehicleRepository.isEnabled());
//            vehicleService.save(vehicle);
//            return "redirect:/" + BASE_URL;
//        }
//    }

    @RequestMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('AUTHORIZER')")
    public String delete(@PathVariable("id") Long id, Model model) {
        Volunteer repositoryUser = volunteerService.findOne(id);
        if (repositoryUser != null) {
            model.addAttribute("vehicle", repositoryUser);
            return VIEWS_VEHICLE_DELETE_CONFIRMATION;
        } else {
            throw new RuntimeException("Invalid User ID" + id);
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('AUTHORIZER')")
    public String disable(@PathVariable("id") Long id) {
        Volunteer vehicle = volunteerService.findOne(id);
        if (vehicle != null) {
            volunteerService.delete(id);
            return "redirect:/" + BASE_URL;
        } else {
            throw new RuntimeException("Invalid User ID" + id);
        }
    }

    @RequestMapping(value = "/file")
    public String file(Pageable pageable, Map<String, Object> model) {
        MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();
        Long userId = user.getId();

        Page<UserJobData> userJobData = userJobRepository.findByUserId(userId, pageable);
        model.put("page", userJobData);
        return "volunteer/file";
    }

    @RequestMapping(value = "/file", method = RequestMethod.POST)
    public String fileSave(@RequestParam("file") MultipartFile file) {
        // Expected to Receive a Zip File Here.
        Path targetPath = storageService.store(file);
        Long userId = Optional.ofNullable(SecurityContextHolder.getContext())
            .map(SecurityContext::getAuthentication)
            .map(Authentication::getPrincipal)
            .map(o -> ((MyUser) o).getId())
            .orElse(-1l);
        FileUploadEvent fileUploadEvent = new FileUploadEvent(this, targetPath, userId);
        applicationEventPublisher.publishEvent(fileUploadEvent);
        return "redirect:/volunteer?upload";
    }

    @RequestMapping(value = "/file/{jobId}/errors", method = RequestMethod.GET)
    public String fileError(Pageable pageable, Map<String, Object> model,
        @PathVariable("jobId") long jobId) {
        UserJobData userJobdata = userJobRepository.getUserJobDataByJobId(jobId);
        Long userId = Optional.ofNullable(SecurityContextHolder.getContext())
            .map(SecurityContext::getAuthentication)
            .map(Authentication::getPrincipal)
            .map(o -> (MyUser) o)
            .map(MyUser::getId)
            .orElse(-2l);
        if (userJobdata != null && userJobdata.getUserId() == userId) {
            Page<FailItems> failItems = failItemService.findByJobId(jobId, pageable);
            model.put("page", failItems);
            return "volunteer/errors";
        }
        throw new RuntimeException("Invalid JobId w.r.t user");
    }

    //    @GetMapping(name = "/search/export")
    @RequestMapping(path = "/search/export", method = RequestMethod.GET)
    public void export(VolunteerSearchDTO searchDTO, HttpServletResponse response)
        throws IOException {
        File file = volunteerService.exportCsv(searchDTO);
        response.setContentLength((int) file.length());
        InputStream inputStream = new FileInputStream(file);
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + "hello.csv" + "\"");
        FileCopyUtils.copy(inputStream, response.getOutputStream());
        response.flushBuffer();
        inputStream.close();
//        return "redirect:/volunteer";
    }
}
