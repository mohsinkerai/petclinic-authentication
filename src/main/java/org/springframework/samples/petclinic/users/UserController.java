package org.springframework.samples.petclinic.users;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserController {

    private final UserRepository userRepository;

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

}
