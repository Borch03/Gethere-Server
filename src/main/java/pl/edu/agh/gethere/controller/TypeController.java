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
 * Created by Dominik on 02.11.2016.
 */

@Controller
@RequestMapping("/admin/type")
public class TypeController {

    final static Logger logger = Logger.getLogger(TypeController.class);

    @Autowired
    RepositoryManager repositoryManager;


    @RequestMapping(value = {"/typesOverview"}, method= RequestMethod.GET)
    public String displayTypesOfLocation(Model model) {
        List<String> types = repositoryManager.getTypes();
        repositoryManager.tearDown();
        logger.info("Successfully got" + types.size() + "types of POI from Repository");

        model.addAttribute("types", types);

        return "/typesOverview";
    }

    @RequestMapping(value = {"/addType"}, method= RequestMethod.POST)
    public String addNewType(String type, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "/addType";
        } else if (repositoryManager.checkIfTypeExists(type)) {
            model.addAttribute("addTypeError", "true");
            return "/addType";
        }
        repositoryManager.addTypeDefinition(type);
        repositoryManager.tearDown();
        logger.info("Successfully added " + type + " type of POI to Repository");

        return "redirect:/typeOverview";
    }
}
