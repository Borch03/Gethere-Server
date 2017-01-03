package pl.edu.agh.gethere.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.edu.agh.gethere.database.RepositoryManager;

import java.util.List;

/**
 * Created by Dominik on 20.10.2016.
 */

@Controller
@RequestMapping("/admin/attribute")
public class AttributeController {

    final static Logger logger = Logger.getLogger(AttributeController.class);

    @RequestMapping(value = {"/attributesOverview"}, method= RequestMethod.GET)
    public String displayAttributeDefinitions(Model model) {
        RepositoryManager repositoryManager = new RepositoryManager();
        List<String> attributeDefinitions = repositoryManager.getAttributeDefinitions();
        repositoryManager.tearDown();
        logger.info("Successfully got " + attributeDefinitions.size() + " POI attribute definitions from Repository");

        model.addAttribute("attributes", attributeDefinitions);

        return "/attributesOverview";
    }

    @RequestMapping(value = {"/addAttribute"}, method= RequestMethod.POST)
    public String addAttributeDefinition(String definition, BindingResult result, Model model) {
        RepositoryManager repositoryManager = new RepositoryManager();
        if (result.hasErrors()) {
            return "/addAttribute";
        } else if (repositoryManager.checkIfAttributeExists(definition)) {
            model.addAttribute("addAttributeError", "true");
            return "/addAttribute";
        }
        repositoryManager.addAttributeDefinition(definition);
        repositoryManager.tearDown();
        logger.info("Successfully added " + definition + " info definition to Repository");

        return "redirect:/attributeOverview";
    }


}
