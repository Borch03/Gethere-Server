package pl.edu.agh.gethere.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.edu.agh.gethere.database.RepositoryManager;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Dominik on 20.10.2016.
 */

@Component
@Path("additional_info")
public class AdditionalPoiInfoController {

    final static Logger logger = Logger.getLogger(AdditionalPoiInfoController.class);

    @POST
    @Consumes("text/plain")
    public Response addAdditionalInfoDefinition(String definition) {

        RepositoryManager repositoryManager = new RepositoryManager();
        repositoryManager.addAdditionalInfoDefinition(definition);
        repositoryManager.tearDown();

        logger.info("Successfully added " + definition + " info definition to Repository");

        return Response.status(200).build();
    }

    @GET
    @Produces("application/json")
    public List<String> getAdditionalInfoDefinitions() {
        RepositoryManager repositoryManager = new RepositoryManager();
        List<String> additionalPoiInfoDefinitions = repositoryManager.getAdditionalInfoDefinitions();
        repositoryManager.tearDown();

        logger.info("Successfully got additional POI info definitions from Repository");

        return additionalPoiInfoDefinitions;
    }

}
