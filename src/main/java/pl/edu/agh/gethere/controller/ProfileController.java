package pl.edu.agh.gethere.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.edu.agh.gethere.database.RepositoryManager;
import pl.edu.agh.gethere.model.User;

/**
 * Created by SG0222581 on 1/2/2017.
 */

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @RequestMapping(value = {"/userInfo"}, method = RequestMethod.GET)
    public String profile(Model model)
    {
        RepositoryManager repositoryManager = new RepositoryManager();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = repositoryManager.getUserByName(username);
        if (user == null) {
            model.addAttribute("error", "You're not logged in.");
            user = new User();
        }
        user.setPassword("");
        model.addAttribute("user", user);
        return "profile/userInfo";
    }

}
