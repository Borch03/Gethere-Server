package pl.edu.agh.gethere.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.edu.agh.gethere.database.RepositoryManager;
import pl.edu.agh.gethere.model.Poi;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * Created by Dominik on 18.06.2016.
 */

@Component
@Path("keyword")
public class KeywordSearchController {

    final static Logger logger = Logger.getLogger(KeywordSearchController.class);

    @POST
    @Consumes("text/plain")
    @Produces("application/json")
    public List<Poi> getKeywordResults(String keyword) {

        RepositoryManager repositoryManager = new RepositoryManager();
        List<Poi> pois = repositoryManager.getKeywordPois(keyword);
        repositoryManager.tearDown();

        logger.info("Found " + pois.size() + " matching POIs");

        return pois;
    }

}
