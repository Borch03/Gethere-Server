package pl.edu.agh.gethere.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.edu.agh.gethere.database.TypeRepositoryManager;

import java.util.List;

/**
 * Created by Dominik on 02.11.2016.
 */

@Controller
@RequestMapping("/admin/type")
public class TypeController {

    final static Logger logger = Logger.getLogger(TypeController.class);

    @RequestMapping(value = {"/typesOverview"}, method= RequestMethod.GET)
    public String displayTypesOfLocation(Model model) {
        TypeRepositoryManager repositoryManager = new TypeRepositoryManager();
        List<String> types = repositoryManager.getTypes();
        repositoryManager.tearDown();
        logger.info("Successfully got" + types.size() + "types of POI from Repository");

        model.addAttribute("types", types);

        return "/admin/type/typesOverview";
    }

    @RequestMapping(value = {"/addType"}, method= RequestMethod.GET)
    public String addNewType(Model model) {
        model.addAttribute("type", new String());
        return "/admin/type/addType";
    }

    @RequestMapping(value = {"/addType"}, method= RequestMethod.POST)
    public String addNewType(String type, Model model) {
        TypeRepositoryManager repositoryManager = new TypeRepositoryManager();
        if (repositoryManager.checkIfTypeExists(type)) {
            model.addAttribute("addTypeError", "true");
            return "/admin/type/addType";
        }
        repositoryManager.addTypeDefinition(type);
        repositoryManager.tearDown();
        logger.info("Successfully added " + type + " type of POI to Repository");
        model.addAttribute("addTypeSuccess", "true");

        return "/admin/type/typesOverview";
    }
}
