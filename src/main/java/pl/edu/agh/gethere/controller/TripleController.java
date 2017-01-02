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
import pl.edu.agh.gethere.model.Triple;

import java.util.List;

/**
 * Created by Dominik on 14.05.2016.
 */

@Controller
@RequestMapping("/triple")
public class TripleController {

    final static Logger logger = Logger.getLogger(TripleController.class);

    @Autowired
    RepositoryManager repositoryManager;

    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity addTriple(List<Triple> triples) {
        triples.forEach(repositoryManager::addStatement);
        repositoryManager.tearDown();

        logger.info("Successfully added triples to Repository");

        return new ResponseEntity<String>(HttpStatus.CREATED);
    }

    @RequestMapping(method=RequestMethod.GET)
    public @ResponseBody List<Triple> getTriples() {
        List<Triple> triples = repositoryManager.getAllTriples();
        repositoryManager.tearDown();

        logger.info("Successfully got all triples from Repository");

        return triples;
    }
}
