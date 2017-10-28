package com.system.demo.users;

import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public String processFindForm(Pageable pageable, Map<String, Object> model) {
        Page<MyUser> users = userRepository.findAll(pageable);
        model.put("page", users);
        return "users/list";
    }

    @RequestMapping(value = "/user/new", method = RequestMethod.GET)
    public String initCreationForm(Map<String, Object> model) {
        MyUser user = new MyUser();
        model.put("user", user);
        return VIEWS_USERS_CREATE_OR_UPDATE_FORM;
    }

    @RequestMapping(value = "/user/new", method = RequestMethod.POST)
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

    @RequestMapping(value = "/user/disable/{id}")
    public String delete(@PathVariable("id") Long id, Model model) {
        MyUser repositoryUser = userRepository.findOne(id);
        if (repositoryUser != null) {
            model.addAttribute("user", repositoryUser);
            return "users/userDelete";
        } else {
            throw new RuntimeException("Invalid User ID" + id);
        }
    }

    @RequestMapping(value = "/user/disable/{id}", method = RequestMethod.POST)
    public String disable(@PathVariable("id") Long id) {
        MyUser repositoryUser = userRepository.findOne(id);
        if (repositoryUser != null) {
            repositoryUser.setEnabled(false);
            userRepository.save(repositoryUser);
            return "redirect:/users";
        } else {
            throw new RuntimeException("Invalid User ID" + id);
        }
    }

    @RequestMapping(value = "/user/edit/{id}/changePassword")
    public String changePassword(@PathVariable("id") Long id, Model model) {
        UserPassword userPassword = new UserPassword();
        model.addAttribute("user", userPassword);
        return "users/userChangePassword";
    }

    @RequestMapping(value = "/user/edit/{id}/changePassword", method = RequestMethod.POST)
    public String changePassword(@PathVariable("id") Long id, @Valid UserPassword user,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "users/userChangePassword";
        }
        // TODO: check user.getId == id
        MyUser repositoryUser = userRepository.findOne(id);
        if (repositoryUser != null) {
            repositoryUser.setPassword(user.getPassword());
            userRepository.save(repositoryUser);
            return "redirect:/users";
        } else {
            throw new RuntimeException("Invalid User ID" + id);
        }
    }
}
