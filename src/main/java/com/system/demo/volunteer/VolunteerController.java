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
import com.system.demo.users.UserAuthority;
import com.system.demo.volunteer.printer.CardPrinter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.system.demo.volunteer.printer.PrintingResult;
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
import org.springframework.validation.BindingResult;
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
  private final CardPrinter cardPrinter;

  private static final String VIEW_ALL = "volunteer/list";
  private static final String SEARCH = "volunteer/search";
  private static final String VIEWS_VEHICLE_DELETE_CONFIRMATION = "volunteer/delete";
  private static final String VIEWS_VOLUNTEER_CREATE_OR_UPDATE_FORM =
      "volunteer/createOrUpdateVehicleForm";

  @Autowired
  public VolunteerController(
      ApplicationEventPublisher applicationEventPublisher,
      VolunteerService volunteerService,
      StorageService storageService,
      FailItemService failItemService,
      UserJobRepository userJobRepository,
      CardPrinter cardPrinter) {
    this.applicationEventPublisher = applicationEventPublisher;
    this.volunteerService = volunteerService;
    this.storageService = storageService;
    this.failItemService = failItemService;
    this.userJobRepository = userJobRepository;
    this.cardPrinter = cardPrinter;
  }

  @RequestMapping(value = "", method = RequestMethod.GET)
  public String processFindForm(Pageable pageable, Map<String, Object> model, SearchDTO searchDTO) {
    Page<Volunteer> vehicles;
    if (searchDTO.getQuery() == null) {
      vehicles = volunteerService.findAllUnPrinted(pageable);
      searchDTO = new SearchDTO();
    } else {
      vehicles = volunteerService.search(searchDTO.getQuery(), pageable);
    }
    model.put("query", searchDTO);
    model.put("page", vehicles);
    long unprintedCount = volunteerService.getUnprintedCount();
    long toBePrinted = volunteerService.getToBePrintedCount();
    model.put("printableCount", toBePrinted);
    model.put("errorCount", unprintedCount - toBePrinted);
    model.put("count", unprintedCount);
    return VIEW_ALL;
  }

  @RequestMapping(value = "search", method = RequestMethod.GET)
  public String search(Pageable pageable, Map<String, Object> model, VolunteerSearchDTO searchDTO) {
    Page<Volunteer> volunteers;
    if (searchDTO.isEmpty()) {
      searchDTO = new VolunteerSearchDTO();
      volunteers = new PageImpl<Volunteer>(Lists.newArrayList());
    } else {
      volunteers = volunteerService.advancedSearch(searchDTO, pageable);
    }
    model.put("total", volunteers.getTotalElements());
    model.put("query", searchDTO);
    model.put("page", volunteers);
    return SEARCH;
  }

  @RequestMapping(value = "/new", method = RequestMethod.GET)
  public String initCreationForm(Map<String, Object> model) {
    Volunteer vehicle = volunteerService.createNew();
    long unprintedCount = volunteerService.getUnprintedCount();
    long toBePrinted = volunteerService.getToBePrintedCount();
    model.put("printableCount", toBePrinted);
    model.put("errorCount", unprintedCount - toBePrinted);
    model.put("count", unprintedCount);
    model.put("vehicle", vehicle);
    return VIEWS_VOLUNTEER_CREATE_OR_UPDATE_FORM;
  }

  @RequestMapping(value = "/new", method = RequestMethod.POST)
  public String processCreationForm(@Valid Volunteer vehicle, BindingResult result)
      throws IOException {
    if (result.hasErrors()) {
      return VIEWS_VOLUNTEER_CREATE_OR_UPDATE_FORM;
    } else {
      vehicle.setEnabled(true);
      volunteerService.save(vehicle);
      return "redirect:/" + BASE_URL + "/new";
    }
  }

  @RequestMapping("/edit/{id}")
  public String edit(@PathVariable("id") Long id, Model model) {
    Volunteer volunteer = volunteerService.findOne(id);
    if (volunteer != null && volunteer.isEnabled() && !volunteer.isVolunteerIsPrinted()) {
      model.addAttribute("vehicle", volunteer);
      long unprintedCount = volunteerService.getUnprintedCount();
      long toBePrinted = volunteerService.getToBePrintedCount();
      model.addAttribute("printableCount", toBePrinted);
      model.addAttribute("errorCount", unprintedCount - toBePrinted);
      model.addAttribute("count", unprintedCount);
      return VIEWS_VOLUNTEER_CREATE_OR_UPDATE_FORM;
    } else {
      throw new RuntimeException("Invalid Voluneer ID " + id);
    }
  }

  @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
  public String editSave(
      @PathVariable("id") Long id,
      @Valid Volunteer volunteer,
      Model model,
      BindingResult bindingResult)
      throws IOException {
    if (bindingResult.hasErrors()) {
      model.addAttribute("vehicle", volunteer);
      return VIEWS_VOLUNTEER_CREATE_OR_UPDATE_FORM;
    } else {
      Volunteer repositoryVolunteer = volunteerService.findOne(id);
      if (repositoryVolunteer == null && repositoryVolunteer.isEnabled()) {
        throw new RuntimeException("Invalid Volunteer Id");
      }
      volunteer.setId(repositoryVolunteer.getId());
      volunteer.setEnabled(repositoryVolunteer.isEnabled());
      volunteer.setVolunteerIsPrinted(repositoryVolunteer.isVolunteerIsPrinted());
      volunteer.setVolunteerIsPrintedDate(repositoryVolunteer.getVolunteerIsPrintedDate());
      volunteer.setPictureAvailable(repositoryVolunteer.isPictureAvailable());
      volunteer.setVolunteerImage(repositoryVolunteer.getVolunteerImage());
      volunteerService.save(volunteer);
      return "redirect:/" + BASE_URL;
    }
  }

  @RequestMapping(value = "/delete/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
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
  @PreAuthorize("hasAuthority('ADMIN')")
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
    MyUser user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long userId = user.getId();

    Page<UserJobData> userJobData = userJobRepository.findByUserId(userId, pageable);
    model.put("page", userJobData);
    return "volunteer/file";
  }

  @RequestMapping(value = "/file", method = RequestMethod.POST)
  public String fileSave(
      @RequestParam("file") MultipartFile file,
      @RequestParam("picture") boolean isPictureAvailable,
      @RequestParam("nicDuplication") boolean allowNicDuplication) {
    // Expected to Receive a Zip File Here.
    Path targetPath = storageService.store(file);
    Long userId =
        Optional.ofNullable(SecurityContextHolder.getContext())
            .map(SecurityContext::getAuthentication)
            .map(Authentication::getPrincipal)
            .map(o -> ((MyUser) o).getId())
            .orElse(-1l);
    if(allowNicDuplication){
        FileUploadEvent fileUploadEvent =
            new FileUploadEvent(this, targetPath, userId, isPictureAvailable,allowNicDuplication);
    } else {
        FileUploadEvent fileUploadEvent =
            new FileUploadEvent(this, targetPath, userId, isPictureAvailable,false);
    }
    FileUploadEvent fileUploadEvent =
        new FileUploadEvent(this, targetPath, userId, isPictureAvailable,allowNicDuplication);
    applicationEventPublisher.publishEvent(fileUploadEvent);
    return "redirect:/volunteer?upload";
  }

  @RequestMapping(value = "/file/{jobId}/errors", method = RequestMethod.GET)
  public String fileError(
      Pageable pageable, Map<String, Object> model, @PathVariable("jobId") long jobId) {
    UserJobData userJobdata = userJobRepository.findOne(jobId);
    MyUser user =
        Optional.ofNullable(SecurityContextHolder.getContext())
            .map(SecurityContext::getAuthentication)
            .map(Authentication::getPrincipal)
            .map(o -> (MyUser) o)
            .get();
    // There should be data and viewer should be admin or user
    if (userJobdata != null
        && (userJobdata.getUserId().equals(user.getId())
            || user.getAuthorities().contains(UserAuthority.ADMIN))) {
      Page<FailItems> failItems = failItemService.findByJobId(userJobdata.getId(), pageable);
      model.put("page", failItems);
      model.put("total", failItems.getTotalElements());
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

  @RequestMapping(path = "file/{jobId}/errors/export", method = RequestMethod.GET)
  public void exportfailItems(
      @PathVariable(name = "jobId") Long jobExecutionId, HttpServletResponse response)
      throws IOException {

    UserJobData userJobdata = userJobRepository.findOne(jobExecutionId);
    MyUser user =
        Optional.ofNullable(SecurityContextHolder.getContext())
            .map(SecurityContext::getAuthentication)
            .map(Authentication::getPrincipal)
            .map(o -> (MyUser) o)
            .get();

    // There should be data and viewer should be admin or user
    if (!(userJobdata != null
        && (userJobdata.getUserId().equals(user.getId())
            || user.getAuthorities().contains(UserAuthority.ADMIN)))) {
      throw new RuntimeException("Invalid JobId w.r.t user");
    }

    File file = failItemService.exportCsv(jobExecutionId);

    response.setContentLength((int) file.length());
    InputStream inputStream = new FileInputStream(file);
    response.setContentType("text/csv");
    response.setHeader(
        "Content-Disposition", "attachment; filename=\"" + "failedItemsCnic.csv" + "\"");
    FileCopyUtils.copy(inputStream, response.getOutputStream());
    response.flushBuffer();
    inputStream.close();
  }

  @RequestMapping(path = "print", method = RequestMethod.GET)
  public String redirectToList() {
    return "redirect:/" + BASE_URL;
  }

  @RequestMapping(path = "print", method = RequestMethod.POST)
  public void man(HttpServletResponse response, HttpServletRequest request) throws Exception {
    List<Volunteer> printableVolunteers = volunteerService.findPrintableVolunteers();
    log.info("Total Received Printable Volunteers {}", printableVolunteers.size());
    if (printableVolunteers.size() > 0) {

      PrintingResult printingResult = cardPrinter.print(printableVolunteers);
      List<Volunteer> printedVolunteers = printingResult.getPrintedVolunteers();

      String pathOfFile = printingResult.getFileName();

      printedVolunteers
          .stream()
          .map(
              volunteer -> {
                volunteer.setVolunteerIsPrinted(true);
                volunteer.setVolunteerIsPrintedDate(new Timestamp(System.currentTimeMillis()));
                return volunteer;
              })
          .forEach(volunteerService::save);

      File file = new File(pathOfFile);
      response.setContentLength((int) file.length());
      InputStream inputStream = new FileInputStream(file);
      response.setContentType("application/pdf");
      // filename=\"" + System.currentTimeMillis() + "printable.pdf"
      response.setHeader("Content-Disposition", "attachment; " + "\"");
      FileCopyUtils.copy(inputStream, response.getOutputStream());
      response.flushBuffer();
      inputStream.close();
    } else {
      response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
      response.setHeader("Location", request.getRequestURL().toString());
    }
  }

  @RequestMapping(path = "/search/print", method = RequestMethod.GET)
  public String redirectList() {
    return "redirect:/" + BASE_URL;
  }

  @RequestMapping(path = "/search/print", method = RequestMethod.POST)
  public void printSearchedRecords(
      HttpServletRequest request, HttpServletResponse response, VolunteerSearchDTO searchDTO)
      throws Exception {
    List<Volunteer> volunteers = volunteerService.advancedSearch(searchDTO);
    List<Volunteer> printableVolunteers =
        volunteers.stream().filter(Volunteer::isValidForPrint).collect(Collectors.toList());
    if (printableVolunteers.size() > 0) {

      PrintingResult printingResult = cardPrinter.print(printableVolunteers);
      List<Volunteer> printedVolunteers = printingResult.getPrintedVolunteers();
      String pathOfFile = printingResult.getFileName();

      printedVolunteers
          .stream()
          .map(
              volunteer -> {
                volunteer.setVolunteerIsPrinted(true);
                volunteer.setVolunteerIsPrintedDate(new Timestamp(System.currentTimeMillis()));
                return volunteer;
              })
          .forEach(volunteerService::save);

      File file = new File(pathOfFile);
      response.setContentLength((int) file.length());
      InputStream inputStream = new FileInputStream(file);
      response.setContentType("application/pdf");
      // filename=\"" + System.currentTimeMillis() + "printable.pdf"
      response.setHeader("Content-Disposition", "attachment; " + "\"");
      FileCopyUtils.copy(inputStream, response.getOutputStream());
      response.flushBuffer();
      inputStream.close();
    } else {
      response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
      response.setHeader("Location", request.getRequestURL().toString());
    }
  }

  @RequestMapping(path = "images/{volunteerId}", method = RequestMethod.GET)
  public void imageShow(
      @PathVariable(name = "volunteerId") Long volunteerId, HttpServletResponse response)
      throws IOException {
    Volunteer one = volunteerService.findOne(volunteerId);
    if (one != null && one.getVolunteerImage() != null) {
      File file = new File(one.getVolunteerImage());
      response.setContentLength((int) file.length());
      InputStream inputStream = new FileInputStream(file);
      response.setContentType("image/jpeg");
      //        response
      //            .setHeader("Content-Disposition", "attachment; filename=\"" + "failedItemsCnic.csv" + "\"");
      FileCopyUtils.copy(inputStream, response.getOutputStream());
      response.flushBuffer();
      inputStream.close();
    }
  }

  @RequestMapping(path = "unprint/{volunteerId}", method = RequestMethod.POST)
  @PreAuthorize("hasAuthority('ADMIN')")
  public String unprint(@PathVariable(name = "volunteerId") long volunteerId) {
    Volunteer volunteer = volunteerService.findOneWithoutImage(volunteerId);
    volunteer.setVolunteerIsPrinted(false);
    volunteerService.save(volunteer);
    return "redirect:/" + BASE_URL;
  }

  @RequestMapping(path = "search/{volunteerId}/unprint", method = RequestMethod.POST)
  @PreAuthorize("hasAuthority('ADMIN')")
  public String unprintSearch(
      @PathVariable(name = "volunteerId") long volunteerId, HttpServletRequest request) {
    Volunteer volunteer = volunteerService.findOneWithoutImage(volunteerId);
    volunteer.setVolunteerIsPrinted(false);
    volunteerService.save(volunteer);
    return "redirect:/" + SEARCH + "?" + request.getQueryString();
  }
}
