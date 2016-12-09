package pl.edu.agh.gethere.controller;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.edu.agh.gethere.database.RepositoryManager;

import java.util.List;

/**
 * Created by Dominik on 02.11.2016.
 */

@Controller
@RequestMapping("/type")
public class TypeController {

    final static Logger logger = Logger.getLogger(TypeController.class);


    @RequestMapping(method= RequestMethod.POST)
    public ResponseEntity addTypeOfPoi(String type) {

        RepositoryManager repositoryManager = new RepositoryManager();
        repositoryManager.addTypeDefinition(type);
        repositoryManager.tearDown();

        logger.info("Successfully added " + type + " type of POI to Repository");

        return new ResponseEntity<String>(HttpStatus.CREATED);
    }

    @RequestMapping(method= RequestMethod.GET)
    public @ResponseBody List<String> getTypes() {
        RepositoryManager repositoryManager = new RepositoryManager();
        List<String> types = repositoryManager.getTypes();
        repositoryManager.tearDown();

        logger.info("Successfully got types of POI from Repository");

        return types;
    }
}
