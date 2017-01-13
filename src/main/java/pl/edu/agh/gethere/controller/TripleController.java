package pl.edu.agh.gethere.controller;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.edu.agh.gethere.database.TripleRepositoryManager;
import pl.edu.agh.gethere.model.Triple;

import java.util.List;

/**
 * Created by Dominik on 14.05.2016.
 */

@Controller
@RequestMapping("/shared/triple")
public class TripleController {

    final static Logger logger = Logger.getLogger(TripleController.class);

    @RequestMapping(value = {"/triplesOverview"}, method=RequestMethod.GET)
    public String displayTriples(Model model) {
        TripleRepositoryManager repositoryManager = new TripleRepositoryManager();
        List<Triple> triples = repositoryManager.getAllTriples();
        repositoryManager.tearDown();

        logger.info("Successfully got all triples from Repository");
        model.addAttribute("triples", triples);

        return "/shared/triple/triplesOverview";
    }
}
