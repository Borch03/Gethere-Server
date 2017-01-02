package pl.edu.agh.gethere.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.edu.agh.gethere.database.RepositoryManager;

import java.util.List;

/**
 * Created by Dominik on 20.10.2016.
 */

@Controller
@RequestMapping("/attribute")
public class AttributeController {

    final static Logger logger = Logger.getLogger(AttributeController.class);

    @Autowired
    RepositoryManager repositoryManager;

    @RequestMapping(method= RequestMethod.POST)
    public ResponseEntity addAttributeDefinition(String definition) {
        repositoryManager.addAttributeDefinition(definition);
        repositoryManager.tearDown();

        logger.info("Successfully added " + definition + " info definition to Repository");

        return new ResponseEntity<String>(HttpStatus.CREATED);
    }

    @RequestMapping(method= RequestMethod.GET)
    public @ResponseBody List<String> getAttributeDefinitions() {
        List<String> attributeDefinitions = repositoryManager.getAttributeDefinitions();
        repositoryManager.tearDown();

        logger.info("Successfully got POI attribute definitions from Repository");

        return attributeDefinitions;
    }

}
