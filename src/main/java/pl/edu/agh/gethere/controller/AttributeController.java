package pl.edu.agh.gethere.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.edu.agh.gethere.database.AttributeRepositoryManager;

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
        AttributeRepositoryManager repositoryManager = new AttributeRepositoryManager();
        List<String> attributeDefinitions = repositoryManager.getAttributeDefinitions();
        repositoryManager.tearDown();
        logger.info("Successfully got " + attributeDefinitions.size() + " POI attribute definitions from Repository");
        model.addAttribute("attributes", attributeDefinitions);

        return "/admin/attribute/attributesOverview";
    }

    @RequestMapping(value = {"/addAttribute"}, method= RequestMethod.GET)
    public String addAttributeDefinition(Model model) {
        model.addAttribute("attribute", new String());
        return "/admin/attribute/addAttribute";
    }

    @RequestMapping(value = {"/addAttribute"}, method= RequestMethod.POST)
    public String addAttributeDefinition(String attribute, Model model) {
        AttributeRepositoryManager repositoryManager = new AttributeRepositoryManager();
        if (repositoryManager.checkIfAttributeExists(attribute)) {
            model.addAttribute("addAttributeError", "true");
            return "/admin/attribute/addAttribute";
        }
        repositoryManager.addAttributeDefinition(attribute);
        repositoryManager.tearDown();
        logger.info("Successfully added " + attribute + " info definition to Repository");
        model.addAttribute("addAttributeSuccess", "true");

        return "/admin/attribute/attributesOverview";
    }


}
