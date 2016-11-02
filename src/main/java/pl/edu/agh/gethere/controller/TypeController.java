package pl.edu.agh.gethere.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.edu.agh.gethere.database.RepositoryManager;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Dominik on 02.11.2016.
 */

@Component
@Path("type")
public class TypeController {

    final static Logger logger = Logger.getLogger(TypeController.class);

    @POST
    @Consumes("text/plain")
    public Response addTypeOfPoi(String type) {

        RepositoryManager repositoryManager = new RepositoryManager();
        repositoryManager.addTypeDefinition(type);
        repositoryManager.tearDown();

        logger.info("Successfully added " + type + " type of POI to Repository");

        return Response.status(200).build();
    }

    @GET
    @Produces("application/json")
    public List<String> getTypes() {
        RepositoryManager repositoryManager = new RepositoryManager();
        List<String> types = repositoryManager.getTypes();
        repositoryManager.tearDown();

        logger.info("Successfully got types of POI from Repository");

        return types;
    }
}
