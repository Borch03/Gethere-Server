package pl.edu.agh.gethere.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thymeleaf.extras.springsecurity4.auth.Authorization;
import org.unbescape.html.HtmlEscape;
import pl.edu.agh.gethere.model.UserRole;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Locale;

/**
 * Created by Dominik on 11.11.2016.
 */

@Controller
@RequestMapping("/")
public class LoginController {

    @RequestMapping(value = {"/"}, method=RequestMethod.GET)
    public String root(Locale locale) {
        return "redirect:/index";
    }

    @RequestMapping(value = {"/index"}, method=RequestMethod.GET)
    public String index(Authentication authentication) {
        if (authentication != null) {
            for (GrantedAuthority auth : authentication.getAuthorities()) {
                if (auth.getAuthority().equals(UserRole.ROLE_ADMIN.toString())) {
                    return "redirect:/admin/index";
                } else if (auth.getAuthority().equals(UserRole.ROLE_USER.toString())) {
                    return "redirect:/user/index";
                }
            }
        }
        return "index";
    }

    @RequestMapping(value = {"/user/index"}, method=RequestMethod.GET)
    public String userIndex() {
        return "user/index";
    }

    @RequestMapping(value = {"/admin/index"}, method=RequestMethod.GET)
    public String adminIndex() {
        return "admin/index";
    }

    @RequestMapping(value = {"/login"}, method=RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = {"/login-error"}, method=RequestMethod.GET)
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    @RequestMapping(value = {"/error"}, method=RequestMethod.GET)
    public String error(HttpServletRequest request, Model model) {
        model.addAttribute("errorCode", "Error " + request.getAttribute("javax.servlet.error.status_code"));
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("<ul>");
        while (throwable != null) {
            errorMessage.append("<li>").append(HtmlEscape.escapeHtml5(throwable.getMessage())).append("</li>");
            throwable = throwable.getCause();
        }
        errorMessage.append("</ul>");
        model.addAttribute("errorMessage", errorMessage.toString());
        return "error";
    }

    @RequestMapping(value = {"/403"}, method=RequestMethod.GET)
    public String forbidden() {
        return "403";
    }

}