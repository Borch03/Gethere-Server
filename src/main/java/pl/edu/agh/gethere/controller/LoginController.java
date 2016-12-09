package pl.edu.agh.gethere.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.unbescape.html.HtmlEscape;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * Created by Dominik on 11.11.2016.
 */

@Controller
@RequestMapping("/")
public class LoginController {

    @RequestMapping(value = {"/"}, method=RequestMethod.GET)
    public String root(Locale locale) {
        return "redirect:/index.html";
    }

    @RequestMapping(value = {"/index"}, method=RequestMethod.GET)
    public String index() {
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

    @RequestMapping(value = {"/shared/index"}, method=RequestMethod.GET)
    public String sharedIndex() {
        return "shared/index";
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

    @RequestMapping(value = {"/simulateError"}, method=RequestMethod.GET)
    public void simulateError() {
        throw new RuntimeException("This is a simulated error message");
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