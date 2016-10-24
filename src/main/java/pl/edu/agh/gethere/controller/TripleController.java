package pl.edu.agh.gethere.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import pl.edu.agh.gethere.database.RepositoryManager;
import pl.edu.agh.gethere.model.Triple;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Dominik on 14.05.2016.
 */

@Component
@Path("triples")
public class TripleController {

    final static Logger logger = Logger.getLogger(TripleController.class);

    @POST
    @Consumes("application/json")
    public Response addTriple(List<Triple> triples) {

        RepositoryManager repositoryManager = new RepositoryManager();
        for (Triple triple : triples) {
            repositoryManager.addStatement(triple);
        }
        repositoryManager.tearDown();

        logger.info("Successfully added triples to Repository");

        return Response.status(200).build();
    }

    @GET
    @Produces("application/json")
    public List<Triple> getTriples() {

        RepositoryManager repositoryManager = new RepositoryManager();
        List<Triple> triples = repositoryManager.getAllTriples();
        repositoryManager.tearDown();

        logger.info("Successfully got all triples from Repository");

        return triples;
    }
}
