package pl.edu.agh.gethere.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.edu.agh.gethere.database.TripleRepositoryManager;
import pl.edu.agh.gethere.model.Poi;

import java.util.List;

/**
 * Created by Dominik on 18.06.2016.
 */

@Controller
@RequestMapping("/keyword")
public class KeywordSearchController {

    final static Logger logger = Logger.getLogger(KeywordSearchController.class);

    @RequestMapping(method= RequestMethod.POST)
    public @ResponseBody List<Poi> getKeywordResults(String keyword) {
        TripleRepositoryManager repositoryManager = new TripleRepositoryManager();
        List<Poi> pois = repositoryManager.getKeywordPois(keyword);
        repositoryManager.tearDown();

        logger.info("Found " + pois.size() + " matching POIs");

        return pois;
    }

}
