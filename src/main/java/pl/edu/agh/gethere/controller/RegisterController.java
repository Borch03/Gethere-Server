package pl.edu.agh.gethere.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.edu.agh.gethere.database.RepositoryManager;
import pl.edu.agh.gethere.model.User;
import pl.edu.agh.gethere.model.UserRole;

import javax.validation.Valid;

/**
 * Created by Dominik on 28.12.2016.
 */

@Controller
@RequestMapping("/register")
public class RegisterController {

    @RequestMapping(method = RequestMethod.GET)
    public String displayRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String addUserFromRegisterForm(@Valid User user, BindingResult result, Model model) {
        RepositoryManager repositoryManager = new RepositoryManager();
        if (result.hasErrors()) {
            return "register";
        } else if (repositoryManager.checkIfUserExists(user.getUsername())) {
            model.addAttribute("addUserError", "true");
            return "register";
        }
        user.setRole(UserRole.ROLE_ADMIN);
        repositoryManager.addUser(user);
        return "redirect:/";
    }
}
