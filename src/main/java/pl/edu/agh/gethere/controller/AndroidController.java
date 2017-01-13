package pl.edu.agh.gethere.controller;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.gethere.database.AttributeRepositoryManager;
import pl.edu.agh.gethere.database.TripleRepositoryManager;
import pl.edu.agh.gethere.database.TypeRepositoryManager;
import pl.edu.agh.gethere.model.Poi;
import pl.edu.agh.gethere.model.Triple;

import java.util.List;

/**
 * Created by SG0222581 on 1/6/2017.
 */

@Controller
@RequestMapping("/android")
public class AndroidController {

    final static Logger logger = Logger.getLogger(AndroidController.class);

    @RequestMapping(value = {"/triple"}, method =RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void addTriple(@RequestBody List<Triple> triples) {
        TripleRepositoryManager repositoryManager = new TripleRepositoryManager();
        triples.forEach(repositoryManager::addStatement);
        repositoryManager.tearDown();

        logger.info("Successfully added triples to Repository");
    }

    @RequestMapping(value = {"/attribute"}, method= RequestMethod.GET)
    public @ResponseBody List<String> getAttributeDefinitions() {
        AttributeRepositoryManager repositoryManager = new AttributeRepositoryManager();
        List<String> attributeDefinitions = repositoryManager.getAttributeDefinitions();
        repositoryManager.tearDown();

        logger.info("Successfully got POI attribute definitions from Repository");

        return attributeDefinitions;
    }

    @RequestMapping(value = {"/type"}, method= RequestMethod.GET)
    public @ResponseBody List<String> getTypes() {
        TypeRepositoryManager repositoryManager = new TypeRepositoryManager();
        List<String> types = repositoryManager.getTypes();
        repositoryManager.tearDown();

        logger.info("Successfully got types of POI from Repository");

        return types;
    }

    @RequestMapping(value = {"/keyword"}, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<Poi> getKeywordResults(@RequestBody String keyword) {
        TripleRepositoryManager repositoryManager = new TripleRepositoryManager();
        List<Poi> pois = repositoryManager.getKeywordPois(keyword);
        repositoryManager.tearDown();

        logger.info("Found " + pois.size() + " matching POIs");

        return pois;
    }
}
