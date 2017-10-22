package org.springframework.samples.petclinic.users;

import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
@Slf4j
public class UserController {

    private final UserRepository userRepository;
    private static final String VIEWS_USERS_CREATE_OR_UPDATE_FORM = "users/createOrUpdateUserForm";

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String processFindForm(Owner owner, BindingResult result, Map<String, Object> model) {

        // find owners by last name
        List<MyUser> users = userRepository.findAll();
        // multiple owners found
        model.put("selections", users);
        return "users/usersList";
    }

    @RequestMapping(value = "/users/new", method = RequestMethod.GET)
    public String initCreationForm(Map<String, Object> model) {
        MyUser user = new MyUser();
        model.put("user", user);
        return VIEWS_USERS_CREATE_OR_UPDATE_FORM;
    }

    @RequestMapping(value = "/users/new", method = RequestMethod.POST)
    public String processCreationForm(@Valid MyUser user, BindingResult result) {
        if (result.hasErrors()) {
            return VIEWS_USERS_CREATE_OR_UPDATE_FORM;
        } else {
            user.setEnabled(true);
            user.setExpired(false);
            user.setLocked(false);
            this.userRepository.save(user);
            return "redirect:/users";
        }
    }

    @RequestMapping("/user/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        MyUser repositoryUser = userRepository.findOne(id);
        if (repositoryUser != null) {
            model.addAttribute("user", repositoryUser);
            return VIEWS_USERS_CREATE_OR_UPDATE_FORM;
        } else {
            throw new RuntimeException("Invalid User ID" + id);
        }
    }

    @RequestMapping(value = "/user/edit/{id}", method = RequestMethod.POST)
    public String editSave(@PathVariable("id") Long id, @Valid MyUser user,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return VIEWS_USERS_CREATE_OR_UPDATE_FORM;
        } else {
            // TODO: check user.getId == id
            MyUser repositoryUser = userRepository.findOne(id);
            if (repositoryUser != null) {
                log.info("User Received {} and repository user {}", user, repositoryUser);
                return "redirect:/users";
            } else {
                throw new RuntimeException("Invalid User ID" + id);
            }
        }
    }

    @RequestMapping(value = "/user/delete/{id}")
    public String delete(@PathVariable("id") Long id, Model model) {
        MyUser repositoryUser = userRepository.findOne(id);
        if (repositoryUser != null) {
            model.addAttribute("user", repositoryUser);
            return "users/userDelete";
        } else {
            throw new RuntimeException("Invalid User ID" + id);
        }
    }
}
