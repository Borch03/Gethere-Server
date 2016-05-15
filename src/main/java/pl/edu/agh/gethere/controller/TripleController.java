package pl.edu.agh.gethere.controller;

import org.springframework.stereotype.Component;
import pl.edu.agh.gethere.model.Triple;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dominik on 14.05.2016.
 */

@Component
@Path("triples")
public class TripleController {

    //TODO - implement getting and adding triples to repository

    @GET
    @Produces("application/json")
    public List<Triple> getTriples() {
        String exampleSubject = "exampleSubject";
        String examplePredicate = "examplePredicate";
        String exampleObject = "exampleObject";

        List<Triple> triples = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Triple triple = new Triple(exampleSubject+i, examplePredicate+i, exampleObject+i);
            triples.add(triple);
        }

        return triples;
    }
}
